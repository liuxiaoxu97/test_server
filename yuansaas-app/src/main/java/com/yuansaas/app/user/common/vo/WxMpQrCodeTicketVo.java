package com.yuansaas.app.user.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信公众号二维码授权code
 *
 * @author HTB 2025/8/22 10:58
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxMpQrCodeTicketVo {

    /**
     * 微信二维码ticket
     */
    private String ticket;

    /**
     * 临时二维码有效时长
     */
    private int expireSeconds;

    /**
     * 临时二维码 sceneId
     * @apiNote 利用该场景值进行后续的轮训查询登录操作
     */
    private String sceneId;

}
