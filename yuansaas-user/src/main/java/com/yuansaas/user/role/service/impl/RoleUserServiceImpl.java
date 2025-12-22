package com.yuansaas.user.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yuansaas.core.redis.RedisUtil;
import com.yuansaas.user.menu.enums.MenuCacheEnum;
import com.yuansaas.user.role.entity.Role;
import com.yuansaas.user.role.entity.RoleUser;
import com.yuansaas.user.role.repository.RoleRepository;
import com.yuansaas.user.role.repository.RoleUserRepository;
import com.yuansaas.user.role.service.RoleUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 角色用户关系的service实现类
 * @author LXZ 2025/10/25 17:16
 */
@Service
@RequiredArgsConstructor
public class RoleUserServiceImpl implements RoleUserService {

    private final RoleUserRepository roleUserRepository;
    private final RoleRepository roleRepository;

    /**
     * 保存或修改
     *
     * @param userId     用户ID
     * @param roleIdList 角色ID列表
     */
    @Override
    @Transactional
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //用户没有一个角色权限的情况
        List<Role> byIdAll = roleRepository.findAllById(roleIdList);
        if(ObjectUtil.isEmpty(byIdAll)){
            return ;
        }
        // 先删除原有关系
        roleUserRepository.deleteByUserId(userId);
        //保存角色用户关系
        List<RoleUser> roleUserList = CollUtil.newArrayList();
        for(Role roleId : byIdAll){
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleId(roleId.getId());
            //保存
            roleUserList.add(roleUser);
        }
        roleUserRepository.saveAll(roleUserList);
    }

    /**
     * 根据角色ids，删除角色用户关系
     *
     * @param roleId 角色id
     */
    @Override
    @Transactional
    public void deleteByRoleIds(Long roleId) {
        // 同步吧用户通过该角色关联的菜单缓存删除
        List<RoleUser> byRoleId = roleUserRepository.findByRoleId(roleId);
        if (!ObjectUtil.isEmpty(byRoleId)) {
            byRoleId.forEach(roleUser -> {
                RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.USER_MENU_LIST.getKey(), roleUser.getUserId()));
            });
        }
        // 删除角色用户关系
        roleUserRepository.deleteByRoleId(roleId);
    }

    /**
     * 根据用户id，删除角色用户关系
     *
     * @param userId 用户ids
     */
    @Override
    @Transactional
    public void deleteByUserIds(Long userId) {
        roleUserRepository.deleteByUserId(userId);
    }

    /**
     * 角色ID列表
     *
     * @param userId 用户ID
     */
    @Override
    public List<Long> getRoleIdList(Long userId) {
        return roleUserRepository.findByUserId(userId).stream().map(RoleUser::getRoleId).toList();
    }
}
