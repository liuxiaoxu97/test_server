package com.yuansaas.user.role.params;

import com.yuansaas.core.page.PageModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * 角色查询参数
 *
 * @author LXZ 2025/10/21 10:48
 */
@Data
public class FindRoleParam extends PageModel {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;
    /**
     * 商家编码
     */
    @NotBlank(message = "商家编码不能为空")
    private String merchantCode;
}
