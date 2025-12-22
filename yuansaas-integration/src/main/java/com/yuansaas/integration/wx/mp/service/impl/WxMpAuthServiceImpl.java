package com.yuansaas.integration.wx.mp.service.impl;

import com.yuansaas.core.utils.RandomUtil;
import com.yuansaas.integration.common.enums.CallType;
import com.yuansaas.integration.common.enums.ServiceType;
import com.yuansaas.integration.common.log.annotations.ThirdPartyLog;
import com.yuansaas.integration.wx.mp.service.WxMpAuthService;
import com.yuansaas.integration.wx.mp.vo.TempQrCodeVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * 微信公众号授权
 *
 * @author HTB 2025/8/18 16:51
 */
@Service
@RequiredArgsConstructor
public class WxMpAuthServiceImpl implements WxMpAuthService {

    private final WxMpService wxMpService;
    private final RandomUtil randomUtil;

    @SneakyThrows
    @Override
    public String getAccessToken()  {
        return wxMpService.getAccessToken();
    }

    @SneakyThrows
    @Override
    public WxMpQrCodeTicket getTempQrCodeTicket(int sceneId, Integer expireSeconds) {
        return wxMpService.getQrcodeService().qrCodeCreateTmpTicket(sceneId, expireSeconds);
    }

//    @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @ThirdPartyLog(serviceName = ServiceType.WX_MP , action = "getTempQrCodeUrlTicket" , callType = CallType.SDK)
    @SneakyThrows
    @Override
    public String getTempQrCodeUrl(String ticket) {
        return wxMpService.getQrcodeService().qrCodePictureUrl(ticket);
    }

    @ThirdPartyLog(serviceName = ServiceType.WX_MP , action = "getTempQrCodeUrl" , callType = CallType.SDK)
    @Override
    public TempQrCodeVo getTempQrCodeUrl() {
        int sceneId = randomUtil.generateRandomInt();
        WxMpQrCodeTicket tempQrCodeTicket = this.getTempQrCodeTicket(sceneId, 5 * 60);
        String tempQrCodeUrl = this.getTempQrCodeUrl(tempQrCodeTicket.getTicket());
        return TempQrCodeVo.builder().url(tempQrCodeUrl)
                .sceneId(String.valueOf(sceneId))
                .build();

    }

    @Override
    public String getAuthorizationUrl(@NonNull String redirectUri, String scope, String state) {
        WxOAuth2Service oAuth2Service = wxMpService.getOAuth2Service();
        return oAuth2Service.buildAuthorizationUrl(redirectUri, WxConsts.OAuth2Scope.SNSAPI_USERINFO, state);
    }

    @SneakyThrows
    @Override
    public WxOAuth2UserInfo handleAuthorizationCallback(String code) {
        WxOAuth2Service oAuth2Service = wxMpService.getOAuth2Service();
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(code);
        WxOAuth2UserInfo userInfo = oAuth2Service.getUserInfo(accessToken, null);
//        eventPublisher.publishEvent(AuthorizationCallbackEvent.builder()
//                        .openid(userInfo.getOpenid())
//                        .city(userInfo.getCity())
//                        .sex(userInfo.getSex())
//                        .unionId(userInfo.getUnionId())
//                        .headImgUrl(userInfo.getHeadImgUrl())
//                        .nickname(userInfo.getNickname())
//                        .mpAppId(wxMpService.getWxMpConfigStorage().getAppId())
//                .build());
        return userInfo;
    }

}
