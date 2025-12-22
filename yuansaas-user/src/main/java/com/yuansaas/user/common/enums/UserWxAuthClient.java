package com.yuansaas.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户微信授权客户端
 *
 * @author HTB 2025/8/26 15:20
 */
@Getter
@AllArgsConstructor
public enum UserWxAuthClient {

    WX_MP_JYWY("简易无忧公众号"),

    ;

    private final String description;
}
