package com.yuansaas.integration.wx.mp.handler;

import com.google.common.base.Strings;
import com.yuansaas.integration.wx.mp.event.model.SubscribeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 关注公众号处理器
 *
 * @author HTB 2025/8/21 18:21
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class SubscribeHandler implements WxMpMessageHandler {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        log.info("新关注用户 OPENID: {}" , wxMessage.getFromUser());
        // 为了结偶 所以使用事件， 在调用方使用@EventListener处理监听器
        eventPublisher.publishEvent(SubscribeEvent.builder()
                .mpAppId(wxMpService.getWxMpConfigStorage().getAppId())
                .toUserName(wxMessage.getToUser())
                .sceneId(Strings.isNullOrEmpty(wxMessage.getEventKey())? null : wxMessage.getEventKey().replace("qrscene_", ""))
                .fromUserOpenId(wxMessage.getFromUser())
                .createTime(LocalDateTime.now()).build());
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .content("欢迎关注小杰").build();
    }

}


