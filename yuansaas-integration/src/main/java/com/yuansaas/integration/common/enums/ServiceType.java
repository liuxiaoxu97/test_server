package com.yuansaas.integration.common.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 服务类型
 *
 * @author HTB 2025/8/13 17:14
 */
@Getter
@RequiredArgsConstructor
public enum ServiceType implements IBaseEnum<ServiceType>{
    WX_MP("微信公众号"),
    WX_MINI("微信小程序"),
    UNKNOWN("未知");

    private final String description;

    @Override
    public String getName() {
        return this.name();
    }
}
