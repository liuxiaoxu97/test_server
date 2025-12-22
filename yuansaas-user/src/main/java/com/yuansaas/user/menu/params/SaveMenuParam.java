package com.yuansaas.user.menu.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 *
 * 保存
 *
 * @author LXZ 2025/10/21 11:50
 */
@Data
public class SaveMenuParam {

    /**
     * 父菜单ID
     */
    @NotNull(message = "父菜单ID不能为空")
    private Long pid = 0L;
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
    private String url;

    /**
     * 授权
     */
    private String permissions;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 菜单类型 0：菜单 1：按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer menuType;
}
