package com.yuansaas.user.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_login_log")
public class UserLoginLog {
    @Id
    @GenericGenerator(
            name = "id",
            strategy = "com.yuansaas.core.jpa.id.CustomIdentityGenerator"
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "id"
    )
    private Long id;

    /**
     * 登录流水号
     */
    private String serialNo;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 登录方式
     */
    private String loginType;

    /**
     * 登录终端
     */
    private String terminal;

    /**
     * 登录IP
     */
    private String remoteIp;

    /**
     * 登录设备
     */
    private String device;

    /**
     * 登录UA
     */
    private String userAgent;

    /**
     * 登录结果
     */
    private String success;

    private String createBy;

    private LocalDateTime createAt;

    /**
     * 备注
     */
    private String remark;

}