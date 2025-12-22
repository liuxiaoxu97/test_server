package com.yuansaas.user.system.entity;

import com.yuansaas.core.jpa.model.BaseEntity;
import com.yuansaas.user.common.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_user")
public class SysUser extends BaseEntity {

    /**
     * 商户编号
     */
    private String merchantCode;
    /**
     * 用户名
     */
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 头像
     */
    private String headUrl;

    /**
     *  手机号
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", length = 50)
    private String realName;
    /**
     * 账户状态
     */
    private String status = UserStatus.active.name();

}