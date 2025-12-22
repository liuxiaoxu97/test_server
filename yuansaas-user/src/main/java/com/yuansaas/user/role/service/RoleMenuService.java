/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.yuansaas.user.role.service;

import java.util.List;

/**
 * 角色菜单关系
 *
 * @author lxz 1.0.0
 */
public interface RoleMenuService {

    /**
     * 保存或修改
     * @param roleId      角色ID
     * @param menuIdList  菜单ID列表
     */
    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ids，删除角色菜单关系
     * @param roleId 角色id
     */
    void deleteByRoleIds(Long roleId);

    /**
     * 根据菜单id，删除角色菜单关系
     * @param menuIds 菜单ids
     */
    void deleteByMenuIds(List<Long> menuIds);

    /**
     * 根据菜单id，查询授权的角色ID列表
     * @param menuIds 菜单ids
     */
    List<Long> getRoleIdListByMenuIds(Long menuIds);

    /**
     * 菜单ID列表
     * @param roleId  角色ID
     */
    List<Long> getMenuIdList(Long roleId);

    /**
     * 菜单ID列表
     * @param roleIds  角色IDs
     */
    List<Long> getMenuIdList(List<Long> roleIds);
}