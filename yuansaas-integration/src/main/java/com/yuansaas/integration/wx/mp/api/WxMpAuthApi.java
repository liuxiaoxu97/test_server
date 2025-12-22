package com.yuansaas.integration.wx.mp.api;

import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.core.utils.id.SnowflakeIdGenerator;
import com.yuansaas.integration.wx.mp.service.WxMpAuthService;
import com.yuansaas.integration.wx.mp.vo.TempQrCodeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 微信公众号授权
 *
 * @author HTB 2025/8/22 09:46
 */
@Slf4j
@RestController
@RequestMapping("/wx/mp/auth")
@RequiredArgsConstructor
public class WxMpAuthApi {

    private final WxMpAuthService wxMpAuthService;
    private final SnowflakeIdGenerator idGenerator;
    /**
     * 微信公众号授权
     *
     * @return 授权链接
     */
    @RequestMapping("/authorize")
    public String authorize() {
        // TODO 微信公众号授权
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    }

    /**
     * 微信公众号临时二维码Ticket
     * todo 用于临时测试，后续需要删除
     * @param sceneId 场景ID
     * @param expireSeconds 过期时间
     * @return 临时二维码
     */
    @Deprecated
    @GetMapping("/qrcode/temp" )
    public ResponseEntity<ResponseModel<WxMpQrCodeTicket>> getTempQrCodeTicket(@RequestParam(value = "sceneId") int sceneId,
                                                                         @RequestParam(value = "expireSeconds", required = false) Integer expireSeconds){
        return ResponseBuilder.okResponse(wxMpAuthService.getTempQrCodeTicket(sceneId, expireSeconds));
    }

    /**
     * 获取临时公众号的二维码URL 根据场景ID和过期时间获取, 5分钟内有效
     * @return 二维码地址
     */
    @GetMapping("/qrcode/temp/url" )
    public ResponseEntity<ResponseModel<TempQrCodeVo>> getTempQrCodeUrl(){
        return ResponseBuilder.okResponse(wxMpAuthService.getTempQrCodeUrl());
    }

    /**
     * 获取授权URL
     * @param redirectUri 跳转地址
     * @param scope 授权范围
     * @param state 状态
     * todo 用于临时测试，后续需要删除
     * @return 授权URL
     */
    @Deprecated
    @GetMapping("/authorization/url")
    public ResponseEntity<ResponseModel<String>> getAuthorizationUrl(@RequestParam(value = "redirectUri") String redirectUri,
                                                                 @RequestParam(value = "scope", required = false) String scope,
                                                                 @RequestParam(value = "state", required = false) String state){
        return ResponseBuilder.okResponse(wxMpAuthService.getAuthorizationUrl(redirectUri, scope, state));
    }

    /**
     * 处理微信授权回调
     * @param code 授权码
     * @return 用户信息
     * todo 用于临时测试，后续需要删除
     */
    @Deprecated
    @PostMapping("/callback")
    public ResponseEntity<ResponseModel<WxOAuth2UserInfo>> handleAuthorizationCallback(@RequestParam("code") String code){
        return ResponseBuilder.okResponse(wxMpAuthService.handleAuthorizationCallback(code));
    }

}
