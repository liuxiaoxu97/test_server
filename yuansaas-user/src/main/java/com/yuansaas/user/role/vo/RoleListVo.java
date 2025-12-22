package com.yuansaas.user.role.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色列表vo
 *
 * @author LXZ 2025/10/21 10:54
 */
@Data
public class RoleListVo {
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
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createAt;
}
