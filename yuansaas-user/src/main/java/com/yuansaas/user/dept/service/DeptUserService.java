package com.yuansaas.user.dept.service;

import java.util.List;

/**
 *
 * 部门用户关系
 *
 * @author LXZ 2025/10/27 16:16
 */
public interface DeptUserService {
    /**
     * 保存或修改
     * @param userId      用户ID
     * @param deptId      部门ID
     */
    void saveOrUpdate(Long userId, Long deptId);

    /**
     * 根据部门ids，删除部门用户关系
     * @param deptId 部门id
     */
    void deleteByDeptIds(Long deptId);

    /**
     * 根据用户id，删除部门用户关系
     * @param userId 用户id
     */
    void deleteByUserId(Long userId);

    /**
     * 部门ID用户列表
     * @param deptId  deptIdID
     */
    List<Long> getUserIdList(Long deptId);

    /**
     * 用户ID查询部门ID
     * @param userId  deptIdID
     */
    Long getDeptIdList(Long userId);
}
