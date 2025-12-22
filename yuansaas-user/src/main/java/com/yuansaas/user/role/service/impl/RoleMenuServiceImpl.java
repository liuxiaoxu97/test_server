package com.yuansaas.user.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.redis.RedisUtil;
import com.yuansaas.user.menu.entity.Menu;
import com.yuansaas.user.menu.enums.MenuCacheEnum;
import com.yuansaas.user.menu.service.MenuService;
import com.yuansaas.user.role.entity.QRoleMenu;
import com.yuansaas.user.role.entity.RoleMenu;
import com.yuansaas.user.role.repository.RoleMenuRepository;
import com.yuansaas.user.role.service.RoleMenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 角色菜单关系的业务实现类
 * @author LXZ 2025/10/25 17:54
 */
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl implements RoleMenuService {

    private final RoleMenuRepository roleMenuRepository;
    private final MenuService menuService;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 保存或修改
     *
     * @param roleId     角色ID
     * @param menuIdList 菜单ID列表
     */
    @Override
    @Transactional
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        // 先删除原有关系
        deleteByRoleIds(roleId);
        // 验证可用的菜单
        List<Menu> menuList = menuService.getByList(menuIdList, AppConstants.N);
        if (ObjectUtil.isEmpty(menuList)) {
            throw DataErrorCode.DATA_NOT_FOUND.buildException("菜单不存在");
        }
        List<RoleMenu> roleMenuList = CollUtil.newArrayList();
        menuList.forEach(menu -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menu.getId());
            roleMenu.setRoleId(roleId);
            roleMenu.setCreateAt(LocalDateTime.now());
            roleMenu.setCreateBy("admin");
            roleMenuList.add(roleMenu);
        });
        roleMenuRepository.saveAll(roleMenuList);
    }

    /**
     * 根据角色ids，删除角色菜单关系
     *
     * @param roleId 角色id
     */
    @Override
    @Transactional
    public void deleteByRoleIds(Long roleId) {
        roleMenuRepository.deleteByRoleId(roleId);
        // 删除角色授权的菜单缓存
        RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.ROLE_MENU_LIST.getKey() , roleId));
    }

    /**
     * 根据菜单id，删除角色菜单关系
     *
     * @param menuIds 菜单ids
     */
    @Override
    @Transactional
    public void deleteByMenuIds(List<Long> menuIds) {
        roleMenuRepository.deleteByMenuIdIn(menuIds);
    }

    /**
     * 根据菜单id，查询授权的角色ID列表
     *
     * @param menuIds 菜单ids
     */
    @Override
    public List<Long> getRoleIdListByMenuIds(Long menuIds) {
        if (ObjectUtil.isEmpty(menuIds)) {
            return null;
        }
        QRoleMenu qRoleMenu = QRoleMenu.roleMenu;
        return jpaQueryFactory.select(qRoleMenu.roleId)
                .from(qRoleMenu)
                .where(qRoleMenu.menuId.eq(menuIds))
                .fetch();
    }

    /**
     * 菜单ID列表
     *
     * @param roleId 角色ID
     */
    @Override
    public List<Long> getMenuIdList(Long roleId) {
        return roleMenuRepository.findMenuIdByRoleId(roleId);
    }

    /**
     * 菜单ID列表
     *
     * @param roleIds 角色IDs
     */
    @Override
    public List<Long> getMenuIdList(List<Long> roleIds) {
        return CollUtil.distinct(roleMenuRepository.findMenuIdByRoleIdIn(roleIds));
    }
}
