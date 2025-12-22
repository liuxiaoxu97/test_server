package com.yuansaas.app.shop.param;

import com.yuansaas.common.model.AddressModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * 添加商家信息
 *
 * @author LXZ 2025/12/12 10:24
 */
@Data
public class UpdateShopParam {

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
     * 商家地址
     */
    @NotNull(message = "商家地址不能为空")
    private AddressModel address;
    /**
     * 统一社会信用代码
     */
    @NotBlank(message = "统一社会信用代码不能为空")
    private String unifiedCreditCode;
    /**
     * 法人姓名
     */
    @NotBlank(message = "法人姓名不能为空")
    private String legalPersonName;
    /**
     * 法人性别
     */
    @NotBlank(message = "法人性别不能为空")
    private String legalPersonSex;
    /**
     * 法人手机号
     */
    @NotBlank(message = "法人手机号不能为空")
    private String legalPersonPhone;
    /**
     * 法人邮箱
     */
    @NotBlank(message = "法人邮箱不能为空")
    private String legalPersonEmail;
    /**
     * 法人身份证正面照片
     */
    @NotBlank(message = "法人身份证正面照片不能为空")
    private String idCardFront;
    /**
     * 法人身份证背面照片
     */
    @NotBlank(message = "法人身份证背面照片不能为空")
    private String idCardBack;
    /**
     * 营业执照
     */
    @NotBlank(message = "营业执照不能为空")
    private String businessLicense;
}
