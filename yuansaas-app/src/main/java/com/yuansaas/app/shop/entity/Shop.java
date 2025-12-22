package com.yuansaas.app.shop.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.yuansaas.common.model.AddressModel;
import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 *
 * @author LXZ 2025/12/12 15:12
 */
@Data
@Entity
@Table(name = "shop")
public class Shop extends BaseEntity {
    /**
     * '店铺名字'
     */
    private String  name;
    /**
     * '店铺类型'
     */
    private String  type;
    /**
     * '店铺编号'
     */
    private String  code;
    /**
     * '店铺logo'
     */
    private String  logo;
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
    @Type(value = JsonStringType.class)
    @Column(columnDefinition = "json")
    private String  label;
    /**
     * '所属行业'
     */
    private String  industry;
    /**
     * '营业开始时间'
     */
    private Date weekdayStartHours;
    /**
     * '营业结束时间'
     */
    private Date  weekdayEndHours;
    /**
     * '省编码'
     */
    private String  provinceCode;
    /**
     * '市编码'
     */
    private String  cityCode;
    /**
     * '市编码'
     */
    private String  districtCode;
    /**
     * '省市区+详细地址[json格式]'
     */
    @Type(value = JsonStringType.class)
    @Column(columnDefinition = "json")
    private AddressModel address;
    /**
     * '法人名字'
     */
    private String  legalPersonName;
    /**
     * '法人手机号'
     */
    private String  legalPersonPhone;
    /**
     * '法人邮箱'
     */
    private String  legalPersonEmail;
    /**
     * '统一信用代码'
     */
    private String  unifiedCreditCode;
    /**
     * '法人身份证正面'
     */
    private String  idCardFront;
    /**
     * '法人身份证反面'
     */
    private String  idCardBack;
    /**
     * '营业执照'
     */
    private String  businessLicense;
    /**
     * '签约人Id'
     */
    private Long  signedUserId;
    /**
     * '签约人'
     */
    private String  signedUserName;
    /**
     * '签约开始时间'
     */
    private LocalDateTime signedStartAt;
    /**
     * '签约结束时间'
     */
    private LocalDateTime  signedEndAt;
    /**
     * '签约金额（分）'
     */
    private Integer  signedAmount;
    /**
     * '签约状态(UNSIGNED [未签约] |SIGNED [已签约] |EXPIRED [已过期])'
     */
    private String  signedStatus;
    /**
     * '锁定状态(Y锁定|N不锁定)'
     */
    private String  lockStatus;
    /**
     * '删除状态(Y删除|N未删除)'
     */
    private String  deleteStatus;
}
