package com.yuansaas.user.auth.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 登录方式枚举
 *
 * @author HTB 2025/8/5 09:54
 */
@Getter
@RequiredArgsConstructor
public enum LoginType implements IBaseEnum<LoginType> {

    USERNAME_PASSWORD("用户名密码"),

    WECHAT("微信"),

    WECHAT_MP_SUBSCRIPTION("微信公众号订阅号"),

    SMS("短信");

    private final String description;

    @Override
    public String getName() {
        return this.name();
    }

}
