package com.yuansaas.user.menu.entity;

import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * 菜单
 *
 * @author LXZ 2025/10/21 11:41
 */
@Data
@Entity
@Table(name = "sys_menu")
public class Menu extends BaseEntity {
    /**
     * 商家code
     */
    private String merchantCode;
    /**
     * 菜单code
     */
    private String menuCode;
    /**
     * 上级ID，一级菜单为0
     */
    private Long pid;
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
     * 类型   0：菜单   1：按钮
     */
    private Integer menuType;
    /**
     * 菜单图标
     */
    private String  icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否锁定 N：未锁定  Y：已锁定
     */
    private String  lockStatus = AppConstants.N;
    /**
     * 是否删除 N：未删除  Y：已删除
     */
    private String  deleteStatus = AppConstants.N;
}
