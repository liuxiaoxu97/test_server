package com.yuansaas.user.menu.vo;

import com.yuansaas.core.model.TreeNode;
import lombok.Data;

import java.util.List;

/**
 *
 * 菜单列表vo
 *
 * @author LXZ 2025/10/21 11:54
 */
@Data
public class MenuListVo extends TreeNode {

    /**
     * 菜单code
     */
    private String menuCode;
    /**
     * 上级ID，一级菜单为0
     */
    private Long   pid;
    /**
     * 名称
     */
    private String name;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 授权(多个用逗号分隔，如：sys:user:list,sys:user:save)
     */
    private String permissions;
    /**
     * 菜单图标
     */
    private String  icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer menuType;
}
