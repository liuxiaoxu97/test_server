package com.yuansaas.integration.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信客户端
 *
 * @author HTB 2025/8/22 16:02
 */
@Getter
@AllArgsConstructor
public enum WxClient {

    WX_MP_JYWY("简易无忧公众号"),

    ;

    private final String description;

}
