package com.yuansaas.app.common.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 平台类型
 *
 * @author LXZ 2025/11/17 16:16
 */
@Getter
@AllArgsConstructor
public enum PlatformTypeEnum implements IBaseEnum<PlatformTypeEnum> {

    /**
     * 公共
     */
    PUBLIC("公共", "-1"),
    ;

    private final String name;
    private final String value;

    @Override
    public String getName() {
        return this.name();
    }
}
