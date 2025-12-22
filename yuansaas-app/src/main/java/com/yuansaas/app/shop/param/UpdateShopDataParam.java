package com.yuansaas.app.shop.param;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.yuansaas.app.shop.enums.ShopTypeEnum;
import com.yuansaas.common.model.AddressModel;
import com.yuansaas.core.annotation.EnumValidate;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;

/**
 *
 * 修改商家基本信息
 *
 * @author LXZ 2025/12/21 18:18
 */
@Data
public class UpdateShopDataParam {

    /**
     * 商家ID
     */
    @NotNull(message = "商家ID不能为空")
    private Long id;
    /**
     * 商家名称
     */
    @NotBlank(message = "商家名称不能为空")
    private String name;
    /**
     * 商家类型
     */
    @EnumValidate(enumClass = ShopTypeEnum.class, message = "商家类型不正确")
    private ShopTypeEnum type;
    /**
     * 商家地址
     */
    @NotNull(message = "商家地址不能为空")
    private AddressModel address;

    /**
     * 商铺logo
     */
    private String logo;
    /**
     * '店铺简介'
     */
    private String  intro;
    /**
     * 手机号
     */
    private String  phone;
    /**
     * 主题色
     */
    private String subjectColor;
    /**
     * '标签'
     */
    private List<String> label;
    /**
     * 客服微信
     */
    private String customerServiceWechat;
    /**
     * 公众号
     */
    private String officialAccounts;



}
