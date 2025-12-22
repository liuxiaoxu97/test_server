package com.yuansaas.user.common.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author HTB 2025/8/5 15:46
 */
@Getter
@AllArgsConstructor
public enum UserStatus implements IBaseEnum<UserStatus> {


    active("活跃"),
    suspended("冻结"),
    deleted("注销");

    private final String description;

    @Override
    public String getName() {
        return this.name();
    }


}
