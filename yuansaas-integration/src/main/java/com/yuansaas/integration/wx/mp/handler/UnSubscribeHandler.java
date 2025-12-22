package com.yuansaas.integration.wx.mp.handler;

import com.google.common.base.Strings;
import com.yuansaas.integration.wx.mp.event.model.UnSubscribeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
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
 * 取关处理器
 *
 * @author HTB 2025/8/21 18:21
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UnSubscribeHandler implements WxMpMessageHandler {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        log.info("取消关注用户 OPENID: {}" , wxMessage.getFromUser());
        // 为了结偶 所以使用事件， 在调用方使用@EventListener处理监听器
        eventPublisher.publishEvent(UnSubscribeEvent.builder()
                .toUserName(wxMessage.getToUser())
                .fromUserOpenId(wxMessage.getFromUser())
                .createTime(LocalDateTime.now())
                .sceneId(Strings.isNullOrEmpty(wxMessage.getEventKey())? null : wxMessage.getEventKey().replace("qrscene_", "")));
        // 因为取消关注，所以无法收到消息，仅用于响应微信，避免重复推送消息
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .content("请别离开，再见！").build();
    }
}
