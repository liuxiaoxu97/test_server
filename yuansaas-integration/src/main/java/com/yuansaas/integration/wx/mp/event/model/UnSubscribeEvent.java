package com.yuansaas.integration.wx.mp.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 取消关注公众号
 *
 * @author HTB 2025/8/21 18:38
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnSubscribeEvent {

    /**
     * 开发者微信号
     */
    private String toUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUserOpenId;

    /**
     * 创建事件
     */
    private LocalDateTime createTime;

    /**
     * 带参数而为码的扫码事件场景ID(sceneId)
     */
    private String sceneId;
}
