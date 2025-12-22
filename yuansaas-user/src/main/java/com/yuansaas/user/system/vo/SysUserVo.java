package com.yuansaas.user.system.vo;

import lombok.Data;

import java.util.List;

/**
 * 系统用户VO
 *
 * @author HTB 2025/8/12 14:24
 */
@Data
public class SysUserVo {

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
     * 角色集合
     */
    private List<Long> roleIds;

}
