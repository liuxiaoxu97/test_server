package com.yuansaas.app.common.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * 字典缓存枚举
 *
 * @author LXZ 2025/11/19 17:25
 */
@Getter
@RequiredArgsConstructor
public enum DictCacheEnum implements IBaseEnum<DictCacheEnum> {

    DICT_ITEM_CACHE("dict_item_cache", "字典项缓存"),

    ;

    private final String code;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }
}
