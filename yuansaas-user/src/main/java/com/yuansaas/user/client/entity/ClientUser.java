package com.yuansaas.user.client.entity;

import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端用户基本信息表
 */
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_user")
public class ClientUser extends BaseEntity implements Serializable {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户名称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码盐
     */
    private String passwordSlat;

    /**
     * 密码哈希值
     */
    private String passwordHash;

    /**
     * 账户余额
     */
    private Long balance;

    /**
     * 注册时间
     */
    private LocalDateTime registrationAt;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 账户状态
     */
    private String status;
    /**
     * 总消费金额
     */
    private Long totalSpent;

}