package com.yuansaas.user.role.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * 角色更新参数
 *
 * @author LXZ 2025/10/21 10:48
 */
@Data
public class UpdateRoleParam {
    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    private Long id;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;
    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空")
    private Long deptId;
    /**
     * 描述
     */
    private String description;
}
