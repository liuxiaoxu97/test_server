package com.yuansaas.user.role.repository;

import com.yuansaas.user.role.entity.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 *
 * @author LXZ 2025/10/25 17:30
 */
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {

    /**
     * 根据角色ID删除角色用户关系
     */
    void deleteByRoleId(Long roleId);
    /**
     * 根据用户ID删除角色用户关系
     */
    void deleteByUserId(Long userId);
    /**
     * 根据用户ID查询角色用户关系
     */
    List<RoleUser> findByUserId(Long userId);

    /**
     * 根据角色ID查询有关系的用户
     */
    List<RoleUser> findByRoleId(Long roleId);
}
