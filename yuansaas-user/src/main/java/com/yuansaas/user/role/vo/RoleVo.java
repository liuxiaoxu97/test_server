package com.yuansaas.user.role.vo;

import lombok.Data;

/**
 *
 * 详情
 *
 * @author LXZ 2025/10/21 10:59
 */
@Data
public class RoleVo {
    /**
     * 角色id
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 部门名称
     */
    private String deptName;
}
