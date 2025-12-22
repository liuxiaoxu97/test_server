package com.yuansaas.app.shop.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 商家类型
 *
 * @author LXZ 2025/12/15 16:43
 */
@Getter
@AllArgsConstructor
public enum ShopTypeEnum implements IBaseEnum<ShopTypeEnum> {

    /**
     * 餐饮
     */
    RESTAURANT("餐饮"),
    /**
     * 景点
     */
    SCENIC_SPOT("景点"),
    /**
     * 美容化妆
     */
    BEAUTY("美容化妆")

    ;

    private final  String name;

}
