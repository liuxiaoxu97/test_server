package com.yuansaas.integration.wx.mp.api;

import com.yuansaas.integration.common.exception.ex.WxErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxRuntimeException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.web.bind.annotation.*;

/**
 * 微信公众号回调
 *
 * @author HTB 2025/8/19 15:22
 */
@Slf4j
@RestController
@RequestMapping("/wx/mp/callback")
@RequiredArgsConstructor
public class WxMpCallbackApi {

    private final WxMpService wxMpService;
    private final WxMpMessageRouter wxMpMessageRouter;

    /**
     * 接受微信公众号回调请求(GET 验证消息的确来自微信服务器)
     * @param request 请求对象
     */
    @GetMapping("/event")
    public String getEvent(HttpServletRequest request) {
        // 验证安全签名
        // token :SyovlIftOJmL4z0DrkiZeH
        if (!wxMpService.checkSignature(request.getParameter("timestamp"), request.getParameter("nonce"), request.getParameter("signature"))) {
            throw new WxRuntimeException("加密消息签名校验失败");
        }
        return request.getParameter("echostr");
    }

    /**
     * 接受微信公众号回调请求
     * @param requestBody 请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 响应
     */
    @PostMapping("/event")
    public String event(@RequestBody String requestBody,
                        @RequestParam("signature") String signature,
                        @RequestParam("timestamp") String timestamp,
                        @RequestParam("nonce") String nonce) {
        log.info("收到微信公众号回调请求:{}" , requestBody);
        // 验证安全签名
        if(!wxMpService.checkSignature(timestamp, nonce, signature)){
            // 处理微信公众号回调请求
            throw WxErrorCode.WX_MP_SIGNATURE_ERROR.buildException();
        }
        // 解析消息体
        WxMpXmlMessage wxMpXmlMessage = WxMpXmlMessage.fromXml(requestBody);
        WxMpXmlOutMessage wxMpXmlOutMessage = null;
        try {
            wxMpXmlOutMessage = wxMpMessageRouter.route(wxMpXmlMessage);
        } catch (Exception e){
            log.error("微信公众号回调处理异常:{}" , e.getMessage());
        }
        return wxMpXmlOutMessage == null ? null : wxMpXmlOutMessage.toXml();
    }

}
