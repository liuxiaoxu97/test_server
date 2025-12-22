package com.yuansaas.user.system.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 创建系统用户参数
 *
 * @author HTB 2025/8/11 15:28
 */
@Data
public class SysUserCreateParam {

    /**
     * 商户编号
     */
    @NotBlank(message = "商户编号不能为空")
    private String merchantCode;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;
    /**
     * 头像
     */
    @NotNull(message = "头像不能为空")
    private String headUrl;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 角色ids
     */
    @NotNull(message = "角色id不能为空")
    private List<Long> roleList;

    /**
     * 部门
     */
    @NotNull(message = "部门id不能为空")
    private Long deptId;
}
