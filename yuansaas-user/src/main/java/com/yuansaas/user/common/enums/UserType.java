package com.yuansaas.user.common.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 * @author HTB 2025/7/22 17:18
 */
@Getter
@AllArgsConstructor
public enum UserType implements IBaseEnum<UserType> {

    SYSTEM_USER("管理员"),
    CLIENT_USER("普通用户"),
    GUEST_USER("访客");

    private final String description;

    @Override
    public String getName() {
        return this.name();
    }
}
