package com.yuansaas.user.role.repository;

import com.yuansaas.user.role.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 *
 * @author LXZ 2025/10/24 14:58
 */
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

    /**
     * 根据角色ID删除角色菜单关系
     */
    void deleteByRoleId(Long roleId);
    /**
     * 根据菜单ID删除角色菜单关系
     */
    void deleteByMenuId(Long menuId);
    /**
     * 根据菜单ID集合删除角色菜单关系
     */
    void deleteByMenuIdIn(List<Long> menuIds);
    /**
     * 根据角色ID查询菜单ID集合
     */
    @Query(value = "SELECT rm.menu_id FROM sys_role_menu rm WHERE rm.role_id = ?1", nativeQuery = true)
    List<Long> findMenuIdByRoleId(Long roleId);
    /**
     * 根据角色ID集合查询菜单ID集合
     */
    @Query(value = "SELECT rm.menu_id FROM sys_role_menu rm WHERE rm.role_id IN ?1", nativeQuery = true)
    List<Long> findMenuIdByRoleIdIn(List<Long> roleIds);
}
