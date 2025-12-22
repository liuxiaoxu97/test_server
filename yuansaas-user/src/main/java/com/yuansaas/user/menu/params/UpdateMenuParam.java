package com.yuansaas.user.menu.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * 保存
 *
 * @author LXZ 2025/10/21 11:50
 */
@Data
public class UpdateMenuParam {
    /**
     * 菜单ID
     */
    @NotNull(message = "菜单ID不能为空")
    private Long id;
    /**
     * 商户编号
     */
    @NotBlank(message = "商户编号不能为空")
    private String merchantCode;
    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String name;
    /**
     * 菜单URL
     */
    @NotBlank(message = "菜单URL不能为空")
    private String url;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单排序
     */
    private Integer sort;
}
