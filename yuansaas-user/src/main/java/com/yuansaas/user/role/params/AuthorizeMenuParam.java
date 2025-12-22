package com.yuansaas.user.role.params;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 *
 * 角色授权
 *
 * @author LXZ 2025/10/24 12:26
 */
@Data
public class AuthorizeMenuParam {
    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    private Long roleId;
    /**
     * 菜单id列表
     */
    @NotNull(message = "菜单id列表不能为空")
    private List<Long> menuIds;
}
