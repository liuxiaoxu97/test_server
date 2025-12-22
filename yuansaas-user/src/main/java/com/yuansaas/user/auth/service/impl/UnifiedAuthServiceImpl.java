package com.yuansaas.user.auth.service.impl;

import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.common.enums.IBaseEnum;
import com.yuansaas.core.exception.ex.AuthErrorCode;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.redis.RedisUtil;
import com.yuansaas.integration.wx.mp.event.model.SubscribeEvent;
import com.yuansaas.user.auth.model.CustomUserDetails;
import com.yuansaas.user.auth.model.UserLockEvent;
import com.yuansaas.user.auth.model.UserUnlockEvent;
import com.yuansaas.user.auth.param.UnifiedLoginParam;
import com.yuansaas.user.auth.properties.JwtProperties;
import com.yuansaas.user.auth.security.TokenBlacklistManager;
import com.yuansaas.user.auth.security.JwtManager;
import com.yuansaas.user.auth.service.UnifiedAuthService;
import com.yuansaas.user.auth.vo.AuthVo;
import com.yuansaas.user.auth.vo.TokenRefreshVo;
import com.yuansaas.user.client.entity.ClientUser;
import com.yuansaas.user.client.service.ClientUserService;
import com.yuansaas.user.common.entity.UserWechatBinding;
import com.yuansaas.user.common.enums.UserStatus;
import com.yuansaas.user.common.enums.UserType;
import com.yuansaas.user.common.enums.UserWxAuthClient;
import com.yuansaas.user.common.model.WechatUserInfoModel;
import com.yuansaas.user.common.service.UserLoginLogService;
import com.yuansaas.user.common.service.WechatBindingService;
import com.yuansaas.user.system.entity.SysUser;
import com.yuansaas.user.system.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.yuansaas.user.common.enums.UserType.CLIENT_USER;
import static com.yuansaas.user.common.enums.UserType.SYSTEM_USER;

/**
 * 统一认证
 *
 * @author HTB 2025/8/8 14:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnifiedAuthServiceImpl implements UnifiedAuthService {

    private final UserLoginLogService loginLogService;
    private final JwtManager jwtManager;
    private final SysUserService sysUserService;
    private final ClientUserService clientUserService;
    private final PasswordEncoder passwordEncoder;
    private final WechatBindingService wechatAuthService;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenBlacklistManager tokenBlacklistManager;
    private final JwtProperties jwtProperties;

    @Override
    public AuthVo login(UnifiedLoginParam loginParam, HttpServletRequest request) {
        try {
            CustomUserDetails userDetails = authenticate(loginParam);

            // 记录登录日志
            loginLogService.logLogin(
                    userDetails,
                    loginParam.getLoginType(),
                    AppConstants.SUCCESS,
                    request
            );

            // 生成令牌
            return generateTokenResponse(userDetails);
        } catch (Exception e) {
            // 记录失败日志
            loginLogService.logLoginFailure(
                    loginParam.getUsername(),
                    loginParam.getUserType(),
                    loginParam.getLoginType(),
                    request,
                    e.getMessage()
            );
            throw e;
        }
    }

    @Override
    public TokenRefreshVo refreshToken(String refreshToken) {

        // 1. 验证刷新令牌
        if (! jwtManager.validateToken(refreshToken)) {
            throw AuthErrorCode.INVALID_TOKEN.buildException("刷新令牌无效");
        }

        // 3. 提取用户信息
        CustomUserDetails userDetails = jwtManager.parseToken(refreshToken);

        // 4. 生成新令牌
        String newAccessToken = jwtManager.generateAccessToken(userDetails);
        String newRefreshToken = jwtManager.generateRefreshToken(userDetails);

        // 5. 将旧刷新令牌加入黑名单
        tokenBlacklistManager.blackListToken(refreshToken);

        log.info("用户 username: {}、userType:{} 刷新令牌成功", userDetails.getUsername(),userDetails.getUserType());

        return TokenRefreshVo.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtProperties.getExpiration() / 1000) // 转换为秒
                .build();
    }

    private CustomUserDetails authenticate(UnifiedLoginParam loginParam) {
        return switch (loginParam.getLoginType()) {
            case USERNAME_PASSWORD -> authenticateByPassword(loginParam);
            case WECHAT -> authenticateByWechat(loginParam);
            case WECHAT_MP_SUBSCRIPTION -> authenticateByWechatMpSubscription(loginParam);
//            case SMS -> authenticateBySms(loginParam);
            default -> throw new AuthenticationServiceException("不支持的登录方式");
        };
    }

    private CustomUserDetails authenticateByPassword(UnifiedLoginParam loginParam) {
        if (loginParam.getUserType() == SYSTEM_USER) {
             SysUser user = sysUserService.findByUsername(loginParam.getUsername())
                    .orElseThrow(() -> AuthErrorCode.AUTHENTICATION_FAILED.buildException("用户名或密码错误"));

             // 加密密码：passwordEncoder.encode(request.getPassword())
            if (!passwordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
                throw AuthErrorCode.AUTHENTICATION_FAILED.buildException("用户名或密码错误") ;
            }

            return new CustomUserDetails(user);
        } else {
            ClientUser user = clientUserService.findByUsername(loginParam.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));

            if (!passwordEncoder.matches(loginParam.getPassword(), user.getPasswordHash())) {
                throw AuthErrorCode.AUTHENTICATION_FAILED.buildException("用户名或密码错误") ;
            }

            return new CustomUserDetails(user);
        }
    }

    /***
     * 关注微信公众号后自动注册登录
     * @param loginParam 登录参数
     * @return CustomUserDetails
     */
    private CustomUserDetails authenticateByWechatMpSubscription(UnifiedLoginParam loginParam) {
         // 公主公众号授权登录时， 在关注公众号的的回调中会将带参数的二维码中的标识sceneId存到Redis中， 在redis中获取微信用户信息
        if(StringUtils.isBlank(loginParam.getWechatMpSceneCode())) {
            throw AuthErrorCode.AUTH_PARAM_ERROR.buildException("微信授权登录参数错误");
        }
        SubscribeEvent wxMpSubscribeInfo = RedisUtil.get(RedisUtil.genKey("WX_MP_SUBSCRIBE_INFO", loginParam.getWechatMpSceneCode()), SubscribeEvent.class);
        if(wxMpSubscribeInfo == null || wxMpSubscribeInfo.getFromUserOpenId() == null) {
            throw AuthErrorCode.AUTH_PARAM_ERROR.buildException("未获取到关注公众号的用户信息");
        }
        // 根据sceneId获取微信用户信息
        WechatUserInfoModel wechatUser = WechatUserInfoModel.builder()
                .wxClient(UserWxAuthClient.WX_MP_JYWY)
                .openid(wxMpSubscribeInfo.getFromUserOpenId())
                .nickname(wxMpSubscribeInfo.getToUserName())
                .build();
        return wxBasicAuthenticate(loginParam, wechatUser);
    }

    /**
     * 微信授权登录基础操作
     * @param loginParam 登录参数
     * @param wechatUser 微信用户信息
     * @return 登录结果
     */
    private CustomUserDetails wxBasicAuthenticate(UnifiedLoginParam loginParam ,  WechatUserInfoModel wechatUser) {
// 查找绑定用户
        UserWechatBinding binding = wechatAuthService.findBindingByOpenid(wechatUser.getOpenid());

        if (binding != null) {
            // todo 已有绑定用户 允许多类型绑定
//            if (loginParam.getUserType().mismatches(binding.getUserType())) {
//                throw new AuthenticationServiceException("微信账号已绑定其他类型用户");
//            }
            Optional<UserType> userTypeOptional = IBaseEnum.fromName(binding.getUserType(), UserType.class);
            UserType bindUserType = userTypeOptional.orElseThrow(() -> AuthErrorCode.ACCESS_DENIED.buildException("未知的绑定用户"));
            return switch (bindUserType) {
                case SYSTEM_USER -> new CustomUserDetails(
                        sysUserService.findById(binding.getUserId())
                                .orElseThrow(() -> DataErrorCode.DATA_NOT_FOUND.buildException("系统用户不存在"))
                );
                case CLIENT_USER -> new CustomUserDetails(
                        clientUserService.findById(binding.getUserId())
                                .orElseThrow(() ->  DataErrorCode.DATA_NOT_FOUND.buildException("客户端用户不存在") )
                );
                default -> throw DataErrorCode.DATA_NOT_FOUND.buildException("未知的绑定用户");
            };
        } else {
            // 新用户自动注册
            return registerNewUserFromWechat(loginParam.getUserType(), wechatUser);
        }
    }

    private CustomUserDetails authenticateByWechat(UnifiedLoginParam loginParam) {
        return wxBasicAuthenticate(loginParam , wechatAuthService.getUserInfo(loginParam.getWechatCode()));
    }
    // todo 短信登录 待实现
//    private CustomUserDetails authenticateBySms(UnifiedLoginParam loginParam) {
//        // 验证短信验证码
//        if (!smsService.verifyCode(loginParam.getUsername(), loginParam.getSmsCode())) {
//            throw new BadCredentialsException("验证码错误或已过期");
//        }
//
//        if (loginParam.getUserType() == SYSTEM_USER) {
//            SysUser user = sysUserService.findByPhone(loginParam.getUsername())
//                    .orElseThrow(() -> new UsernameNotFoundException("系统用户不存在"));
//            return new CustomUserDetails(user);
//        } else {
//            ClientUser user = clientUserService.findByPhone(loginParam.getUsername())
//                    .orElseThrow(() -> new UsernameNotFoundException("客户端用户不存在"));
//            return new CustomUserDetails(user);
//        }
//    }

    private CustomUserDetails registerNewUserFromWechat(UserType userType, WechatUserInfoModel wechatUser) {
        if (userType == SYSTEM_USER) {
            // 系统用户不允许自动注册
            throw AuthErrorCode.UNAUTHORIZED.buildException("系统用户需要管理员创建");
        }

        // 创建新客户端用户
        ClientUser newUser = ClientUser.builder()
                .userName("wx_" + wechatUser.getOpenid().substring(0, 8))
                .nickName(wechatUser.getNickname())
                .avatarUrl(wechatUser.getAvatar())
                .status(UserStatus.active.name())
                .build();

        ClientUser savedUser = clientUserService.save(newUser);

        // 绑定微信
        wechatAuthService.bindWechat(savedUser.getId(), CLIENT_USER, wechatUser);

        return new CustomUserDetails(savedUser);
    }

    private AuthVo generateTokenResponse(CustomUserDetails userDetails) {
        return AuthVo.builder()
                .accessToken(jwtManager.generateAccessToken(userDetails))
                .refreshToken(jwtManager.generateRefreshToken(userDetails))
                .userType(userDetails.getUserType())
                .userId(userDetails.getUserId())
                .username(userDetails.getUsername())
                .build();
    }

    @Override
    public void logout(String token) {
        // 移除Bearer 前缀
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        tokenBlacklistManager.blackListToken(token);
    }

    @Transactional
    @Override
    public void lockUser(Long userId, UserType userType) {
        if (userType == SYSTEM_USER) {
            sysUserService.lockUser(userId);
        }else if (userType == CLIENT_USER) {
            clientUserService.lockUser(userId);
        }
        // 发布用户锁定事件
        eventPublisher.publishEvent(new UserLockEvent(userId, userType));
    }

    @Transactional
    @Override
    public void unlockUser(Long userId, UserType userType) {
        if (userType == SYSTEM_USER) {
            sysUserService.unlockUser(userId);
        }else if (userType == CLIENT_USER) {
            clientUserService.unlockUser(userId);
        }
        // 发布用户解锁事件
        eventPublisher.publishEvent(new UserUnlockEvent(userId, userType));
    }

    @Override
    public void disableUser(Long userId, UserType userType) {

    }

    @Override
    public void enableUser(Long userId, UserType userType) {

    }
}
