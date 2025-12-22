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
 * 角色用户关系
 *
 * @author lxz 1.0.0
 */
public interface RoleUserService {

    /**
     * 保存或修改
     * @param userId      用户ID
     * @param roleIdList  角色ID列表
     */
    void saveOrUpdate(Long userId, List<Long> roleIdList);

    /**
     * 根据角色ids，删除角色用户关系
     * @param roleIds 角色ids
     */
    void deleteByRoleIds(Long roleIds);

    /**
     * 根据用户id，删除角色用户关系
     * @param userId 用户id
     */
    void deleteByUserIds(Long userId);

    /**
     * 角色ID列表
     * @param userId  用户ID
     */
    List<Long> getRoleIdList(Long userId);
}