package com.yuansaas.user.auth.service;

import com.yuansaas.user.auth.param.UnifiedLoginParam;
import com.yuansaas.user.auth.vo.AuthVo;
import com.yuansaas.user.auth.vo.TokenRefreshVo;
import com.yuansaas.user.common.enums.UserType;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 统一认证接口
 *
 * @author HTB 2025/8/8 14:53
 */
public interface UnifiedAuthService {

    /**
     * 登录
     * @param loginParam 登录参数
     * @param httpServletRequest http请求
     * @return 登录结果
     */
    AuthVo login(UnifiedLoginParam loginParam , HttpServletRequest  httpServletRequest);

    /**
     * 刷新token
     * @param refreshToken 刷新token
     * @return 刷新token结果
     */
    TokenRefreshVo refreshToken(String refreshToken);


    /**
     * 登出
     * @param token token
     */
    void logout(String token);

    /**
     * 锁定用户
     * @param userId 用户id
     * @param userType 用户类型
     */
    void lockUser(Long userId , UserType userType);

    /**
     * 解锁用户
     * @param userId 用户id
     * @param userType 用户类型
     */
    void unlockUser(Long userId , UserType userType);

    /**
     * 禁用用户
     * @param userId 用户id
     * @param userType 用户类型
     */
    void disableUser(Long userId , UserType userType);

    /**
     * 启用用户
     * @param userId 用户id
     * @param userType 用户类型
     */
    void enableUser(Long userId , UserType userType);

}
