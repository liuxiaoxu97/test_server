package com.yuansaas.user.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.AuthErrorCode;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.jpa.querydsl.BoolBuilder;
import com.yuansaas.core.page.RPage;
import com.yuansaas.core.redis.RedisUtil;
import com.yuansaas.core.utils.TreeUtils;
import com.yuansaas.user.common.enums.UserStatus;
import com.yuansaas.user.dept.entity.QSysDept;
import com.yuansaas.user.dept.entity.QSysDeptUser;
import com.yuansaas.user.dept.service.DeptUserService;
import com.yuansaas.user.menu.entity.Menu;
import com.yuansaas.user.menu.enums.MenuCacheEnum;
import com.yuansaas.user.menu.service.MenuService;
import com.yuansaas.user.menu.vo.MenuListVo;
import com.yuansaas.user.role.service.RoleMenuService;
import com.yuansaas.user.role.service.RoleUserService;
import com.yuansaas.user.system.entity.QSysUser;
import com.yuansaas.user.system.entity.SysUser;
import com.yuansaas.user.system.param.FindUserParam;
import com.yuansaas.user.system.param.SysUserCreateParam;
import com.yuansaas.user.system.param.UpdateUserPwdParam;
import com.yuansaas.user.system.param.UserUpdateParam;
import com.yuansaas.user.system.repository.SysUserRepository;
import com.yuansaas.user.system.service.SysUserService;
import com.yuansaas.user.system.vo.SysUserListVo;
import com.yuansaas.user.system.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 系统用户接口
 *
 * @author HTB 2025/8/8 14:41
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleUserService roleUserService;
    private final RoleMenuService roleMenuService;
    private final DeptUserService deptUserService;
    private final MenuService menuService;
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<SysUser> findById(Long id) {
        // todo 优化查询 改成先查询缓存
        return sysUserRepository.findById( id);
    }

    /**
     * 通过id查询系统用户并关联的角色、部门信息
     *
     * @param id id
     * @return 系统用户
     */
    @Override
    public SysUserVo findLinkDateById(Long id) {
        SysUser sysUser = findById(id).orElse(null);
        if (ObjectUtil.isEmpty(sysUser)) {
            return null;
        }
        // 查询部门id
        Long deptId = deptUserService.getDeptIdList(id);
        // 查询角色id
        List<Long> roleIdList = roleUserService.getRoleIdList(id);
        // 查询菜单列表
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtil.copyProperties(sysUser , sysUserVo);
        sysUserVo.setDeptId(deptId);
        sysUserVo.setRoleIds(roleIdList);
        return sysUserVo;
    }

    @Override
    public Optional<SysUser> findByUsername(String username) {
        return sysUserRepository.findByUserName(username);
    }

    /**
     * 创建用户
     *
     * @param sysUserCreateParam 用户创建请求
     * @return 创建成功的用户信息
     */
    @Override
    public SysUser saveUser(SysUserCreateParam sysUserCreateParam) {
        // 保存用户信息
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserCreateParam, sysUser);
        sysUser.setPassword(passwordEncoder.encode(sysUserCreateParam.getPassword()));
        sysUser.setCreateAt(LocalDateTime.now());
        sysUser.setCreateBy(AppContextUtil.getUserInfo());
        sysUserRepository.save(sysUser);
        // 授权角色权限
        roleUserService.saveOrUpdate(sysUser.getId(), sysUserCreateParam.getRoleList());
        // 授权部门权限
        deptUserService.saveOrUpdate(sysUser.getId(),sysUserCreateParam.getDeptId());
        return sysUser;
    }

    @Override
    public Boolean updateUser(UserUpdateParam userUpdateParam) {
        sysUserRepository.findById(userUpdateParam.getId()).ifPresentOrElse(sysUser -> {
            BeanUtils.copyProperties(userUpdateParam, sysUser);
            sysUser.setUpdateBy(AppContextUtil.getUserInfo());
            sysUser.setUpdateAt(LocalDateTime.now());
            sysUserRepository.save(sysUser);
            // 授权角色权限
            roleUserService.saveOrUpdate(sysUser.getId(), userUpdateParam.getRoleList());
            // 授权部门权限
            deptUserService.saveOrUpdate(sysUser.getId(),userUpdateParam.getDeptId());
        },()->{
            throw  DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
        });
        return true;
    }

    /**
     * 修改密码
     *
     * @param updateUserPwd 用户修改请求
     * @return 修改成功的用户信息
     */
    @Override
    public Boolean updateUserPwd(UpdateUserPwdParam updateUserPwd) {
        sysUserRepository.findById(updateUserPwd.getUserId()).ifPresentOrElse(sysUser -> {
                        // 加密密码：passwordEncoder.encode(request.getPassword())
                        if (!passwordEncoder.matches(updateUserPwd.getOldPassword(), sysUser.getPassword())) {
                            throw AuthErrorCode.AUTHENTICATION_FAILED.buildException("旧密码输入错误，请重新输入") ;
                        }
                        sysUser.setPassword(updateUserPwd.getNewPassword());
                        sysUser.setUpdateAt(LocalDateTime.now());
                        sysUser.setUpdateBy(AppContextUtil.getUserInfo());
                        sysUserRepository.save(sysUser);
                    }
                    ,() ->{
                        throw  DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
                    }
        );
        return null;
    }

    /**
     * 重置密码
     *
     * @param id 用户修改请求
     * @return 修改成功的用户信息
     */
    @Override
    public Boolean resetUserResetPwd(Long id) {
        sysUserRepository.findById(id).ifPresentOrElse(sysUser -> {
                    sysUser.setPassword(AppConstants.PWD);
                    sysUser.setUpdateAt(LocalDateTime.now());
                    sysUser.setUpdateBy(AppContextUtil.getUserInfo());
                    sysUserRepository.save(sysUser);
                }
                ,() ->{
                    throw  DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
                }
        );
        return null;
    }

    @Override
    public Boolean lockUser(Long userId) {
        sysUserRepository.findById(userId).ifPresentOrElse(sysUser -> {
            sysUser.setStatus(UserStatus.suspended.name());
            sysUser.setUpdateAt(LocalDateTime.now());
            sysUser.setUpdateBy(AppContextUtil.getUserInfo());
            sysUserRepository.save(sysUser);
        }, () -> {
            throw  DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
        });
        return true;
    }

    @Override
    public Boolean unlockUser(Long userId) {
        sysUserRepository.findById(userId).ifPresentOrElse(sysUser -> {
            sysUser.setStatus(UserStatus.active.name());
            sysUser.setUpdateAt(LocalDateTime.now());
            sysUser.setUpdateBy(AppContextUtil.getUserInfo());
            sysUserRepository.save(sysUser);
        }, () -> {
            throw  DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
        });
        return true;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        sysUserRepository.findById(userId).ifPresentOrElse(sysUser -> {
            sysUser.setStatus(UserStatus.deleted.name());
            sysUser.setUpdateAt(LocalDateTime.now());
            sysUser.setUpdateBy(AppContextUtil.getUserInfo());
            sysUserRepository.save(sysUser);
            // 解除角色权限
            roleUserService.deleteByUserIds(userId);
            // 解除部门权限
            deptUserService.deleteByUserId(userId);
            // 删除菜单缓存
            RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.USER_MENU_LIST, userId));
        }, () -> {
            throw  DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
        });
        return true;
    }

    /**
     * 根据用户id查询菜单列表
     *
     * @param userId 用户id
     * @return 菜单列表
     */
    @Override
    public List<MenuListVo> findMenuListByUserId(Long userId) {
        return RedisUtil.getOrLoad(RedisUtil.genKey(MenuCacheEnum.USER_MENU_LIST, userId), new TypeReference<List<MenuListVo>>() {}, () -> {
            // 查询角色列表
            List<Long> roleIdList = roleUserService.getRoleIdList(userId);
            // 查询菜单列表
            List<Long> menuIdList = roleMenuService.getMenuIdList(roleIdList);
            // 构建树形菜单
            List<Menu> menuList = menuService.getByList(menuIdList,AppConstants.N);
            return TreeUtils.build(BeanUtil.copyToList(menuList, MenuListVo.class), AppConstants.ZERO_L);
        });
    }

    /**
     * 列表查询
     *
     * @param findUserParam 查询参数
     * @return roleListVo
     */
    @Override
    public RPage<SysUserListVo> getByPage(FindUserParam findUserParam) {

        List<String> status ;
        if (ObjectUtil.isEmpty(findUserParam.getStatus())) {
            status = List.of(UserStatus.active.name(),UserStatus.suspended.name());
        } else {
            status = List.of(findUserParam.getStatus().getName());
        }

        QSysUser sysUser = QSysUser.sysUser;
        QSysDept qSysDept = QSysDept.sysDept;
        QSysDeptUser qSysDeptUser = QSysDeptUser.sysDeptUser;
        QueryResults<SysUserListVo> page = jpaQueryFactory.select(Projections.bean(SysUserListVo.class,
                        sysUser.id,
                        sysUser.userName,
                        sysUser.realName,
                        sysUser.phone,
                        sysUser.email,
                        sysUser.status,
                        sysUser.createAt,
                        sysUser.createBy,
                        sysUser.updateAt,
                        sysUser.updateBy,
                        qSysDept.id.as("deptId"),
                        qSysDept.name.as("deptName")))
                .from(sysUser)
                .leftJoin(qSysDeptUser).on(sysUser.id.eq(qSysDeptUser.userId))
                .leftJoin(qSysDept).on(qSysDeptUser.deptId.eq(qSysDept.id))
                .where(
                        BoolBuilder.getInstance()
                                .and(findUserParam.getUserName(), sysUser.userName::contains)
                                .and(findUserParam.getPhone(), sysUser.phone::eq)
                                .and(sysUser.status.in(status))
                                .getWhere()
                ).orderBy(sysUser.createAt.desc())
                .offset(findUserParam.obtainOffset())
                .limit(findUserParam.getPageSize())
                .fetchResults();
        return findUserParam.getRPage(page.getResults(),page.getTotal());
    }
}
