package com.yuansaas.integration.wx.mp.service;

import com.yuansaas.integration.wx.mp.vo.TempQrCodeVo;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * 微信公众号授权相关接口
 *
 * @author HTB 2025/8/18 15:43
 */
public interface WxMpAuthService {

    /**
     * 获取微信公众号的access_token
     * @return access_token
     */
    String getAccessToken() ;

    /**
     * 获取临时公众号的二维码ticket
     * @param sceneId 场景ID
     * @param expireSeconds 过期时间(秒)
     * @return 二维码地址
     */
    WxMpQrCodeTicket getTempQrCodeTicket(int sceneId, Integer expireSeconds);

    /**
     * 获取临时公众号的二维码URL 根据获取的二维码ticket获取
     * @param ticket 二维码ticket
     * @return 二维码地址
     */
    String getTempQrCodeUrl(String ticket);

    /**
     * 获取临时公众号的二维码URL 根据场景ID和过期时间获取 默认5分钟过期
     * @return 二维码地址
     */
    TempQrCodeVo getTempQrCodeUrl();
    /**
     * 获取公众号授权URL 用于引导用户授权
     * @param redirectUri 授权成功后的回调地址
     * @param scope 授权范围  @See WxConsts.OAuth2Scope
     * @param state 状态参数
     * @return 授权URL
     */
    String getAuthorizationUrl(String redirectUri, String scope, String state);

    /**
     * 授权URL回调处理
     * @param code 授权码
     * @return 授权信息
     */
    WxOAuth2UserInfo handleAuthorizationCallback(String code);

}
