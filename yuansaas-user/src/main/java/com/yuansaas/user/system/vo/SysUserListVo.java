package com.yuansaas.user.system.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户VO
 *
 * @author HTB 2025/8/12 14:24
 */
@Data
public class SysUserListVo {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     *  手机号
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 账户状态
     */
    private String status;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 部门名字
     */
    private String deptName;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createAt;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateAt;
}
