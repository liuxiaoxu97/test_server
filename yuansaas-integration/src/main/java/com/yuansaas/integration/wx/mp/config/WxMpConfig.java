package com.yuansaas.integration.wx.mp.config;

import com.yuansaas.integration.wx.mp.handler.SubscribeHandler;
import com.yuansaas.integration.wx.mp.handler.UnSubscribeHandler;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信公众号配置
 *
 * @author HTB 2025/8/21 18:12
 */
@RequiredArgsConstructor
@Configuration
public class WxMpConfig {

    private final WxMpService wxMpService;
    private final SubscribeHandler subscribeHandler;
    private final UnSubscribeHandler unSubscribeHandler;


    @Bean
    public WxMpMessageRouter wxMpMessageRouter() {
        // 创建消息路由
        final WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        // 添加关注事件推送路由
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler).end();
        // 用户已关注时的事件推送
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SCAN).handler(subscribeHandler).end();
        // 添加取消关注事件推送路由
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.UNSUBSCRIBE).handler(unSubscribeHandler).end();
        return router;
    }

}
