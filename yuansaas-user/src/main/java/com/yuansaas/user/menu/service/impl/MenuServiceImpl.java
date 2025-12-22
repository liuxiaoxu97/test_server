package com.yuansaas.user.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.core.context.AppContextHolder;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.jpa.querydsl.BoolBuilder;
import com.yuansaas.core.redis.RedisUtil;
import com.yuansaas.core.utils.TreeUtils;
import com.yuansaas.user.config.ServiceManager;
import com.yuansaas.user.menu.entity.Menu;
import com.yuansaas.user.menu.entity.QMenu;
import com.yuansaas.user.menu.enums.MenuCacheEnum;
import com.yuansaas.user.menu.model.MenuModel;
import com.yuansaas.user.menu.params.FindMenuParam;
import com.yuansaas.user.menu.params.SaveMenuParam;
import com.yuansaas.user.menu.params.UpdateMenuParam;
import com.yuansaas.user.menu.repository.MenuRepository;
import com.yuansaas.user.menu.service.MenuService;
import com.yuansaas.user.menu.vo.MenuListVo;
import com.yuansaas.user.menu.vo.MenuVo;
import com.yuansaas.user.role.entity.QRoleMenu;
import com.yuansaas.user.role.entity.QRoleUser;
import com.yuansaas.user.role.service.RoleMenuService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * 菜单服务
 *
 * @author LXZ 2025/10/21 12:02
 */
@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 列表查询
     *
     * @param findMenuParam 查询参数
     *
     */
    @Override
    public List<MenuListVo> list(FindMenuParam findMenuParam) {
     return    RedisUtil.getOrLoad(RedisUtil.genKey(MenuCacheEnum.MENU_LIST.getName(),findMenuParam.getMerchantCode()), new TypeReference<List<MenuListVo>>() {
        }, () -> {
            QMenu menu = QMenu.menu;
            List<MenuListVo> listVos = jpaQueryFactory.select(Projections.bean(MenuListVo.class,
                            menu.id,
                            menu.name,
                            menu.url,
                            menu.permissions,
                            menu.icon,
                            menu.sort,
                            menu.menuType,
                            menu.pid,
                            menu.merchantCode,
                            menu.createAt,
                            menu.updateAt,
                            menu.deleteStatus,
                            menu.lockStatus,
                            menu.createBy,
                            menu.updateBy))
                    .from(menu)
                    .where(BoolBuilder.getInstance()
                            .and(findMenuParam.getMerchantCode(), menu.merchantCode::eq)
                            .and(findMenuParam.getMenuType(), menu.menuType::eq)
                            .and(menu.deleteStatus.eq(AppConstants.N))
                            .getWhere())
                    .fetch();
            // 构建树形结构
            return TreeUtils.build(listVos, AppConstants.ZERO_L);
        });

    }

    /**
     * 新增菜单
     *
     * @param saveMenuParam 新增参数
     */
    @Override
    public Boolean save(SaveMenuParam saveMenuParam) {
        // 校验参数
        MenuModel menuModel = new MenuModel();
        BeanUtils.copyProperties(saveMenuParam, menuModel);
        Menu menu = validated(menuModel);
        // 获取菜单编码
        String menuCode = getMenuCode(ObjectUtil.isEmpty(menu) ? "":menu.getMenuCode(),saveMenuParam.getMerchantCode());

        // 保存菜单
        Menu menuNew = new Menu();
        BeanUtils.copyProperties(saveMenuParam, menuNew);
        menuNew.setMenuCode(menuCode);
        menuNew.setCreateAt(LocalDateTime.now());
        menuNew.setCreateBy(AppContextUtil.getUserInfo());
        menuRepository.save(menuNew);
        // 清除缓存
        RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.MENU_LIST.getName(),saveMenuParam.getMerchantCode()));
        return true;
    }

    /**
     * 修改菜单
     *
     * @param updateMenuParam 修改参数
     */
    @Override
    public Boolean update(UpdateMenuParam updateMenuParam) {
        menuRepository.findById(updateMenuParam.getId()).ifPresentOrElse(menu->{
            menu.setName(updateMenuParam.getName());
            menu.setUrl(updateMenuParam.getUrl());
            menu.setSort(updateMenuParam.getSort());
            menu.setIcon(updateMenuParam.getIcon());
            menu.setUpdateAt(LocalDateTime.now());
            menu.setUpdateBy("admin");
            menuRepository.save(menu);
            // 清除缓存
            RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.MENU_LIST.getName(),menu.getMerchantCode()));
            isMenuAffectingUserid(menu.getId());
        } ,()->{
            throw DataErrorCode.DATA_NOT_FOUND.buildException("菜单不存在");
        });
        return true;
    }

    /**
     * 删除菜单
     *
     * @param id    菜单id
     */
    @Override
    public Boolean delete(Long id) {
        menuRepository.findById(id).ifPresentOrElse(menu->{
            menu.setDeleteStatus(AppConstants.N.equals(menu.getDeleteStatus()) ?  AppConstants.Y : AppConstants.N);
            menu.setUpdateAt(LocalDateTime.now());
            menu.setUpdateBy("admin");
            menuRepository.save(menu);
            // 清除缓存
            RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.MENU_LIST.getName(),menu.getMerchantCode()));
            isMenuAffectingUserid(menu.getId());
        } ,()->{
            throw DataErrorCode.DATA_NOT_FOUND.buildException("菜单不存在");
        });
        return true;
    }

    /**
     * 禁用菜单
     *
     * @param id 菜单id
     */
    @Override
    public Boolean lock(Long id) {
        menuRepository.findById(id).ifPresentOrElse(menu->{
            menu.setLockStatus(AppConstants.N.equals(menu.getLockStatus()) ?  AppConstants.Y : AppConstants.N);
            menu.setUpdateAt(LocalDateTime.now());
            menu.setUpdateBy("admin");
            menuRepository.save(menu);
            isMenuAffectingUserid(menu.getId());
        } ,()->{
            throw DataErrorCode.DATA_NOT_FOUND.buildException("菜单不存在");
        });
        return true;
    }

    /**
     * 菜单详情
     *
     * @param id 菜单id
     */
    @Override
    public MenuVo getById(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> DataErrorCode.DATA_NOT_FOUND.buildException("菜单不存在"));
        MenuVo menuVo = new MenuVo();
        BeanUtils.copyProperties(menu, menuVo);
        return menuVo;
    }

    /**
     * 批量获取菜单 (原始信息)
     * @param lockStatus     锁定状态
     * @param ids            菜单id列表
     * @return 菜单列表
     */
    @Override
    public List<Menu> getByList(List<Long> ids, String lockStatus) {
        QMenu menu = QMenu.menu;
        return jpaQueryFactory.select(menu)
                .from(menu)
                .where(BoolBuilder.getInstance()
                        .and(lockStatus, menu.lockStatus::eq)
                        .and(ids, menu.id::in)
                        .getWhere()
                ).fetch();
    }

    /**
     * 验证父级菜单是否存在
     * @param menuModel 校验model
     */
    public Menu validated(MenuModel menuModel) {
        if (menuModel.getMenuType() == AppConstants.ZERO && ObjectUtil.isNull(menuModel.getUrl())) {
            throw DataErrorCode.DATA_VALIDATION_FAILED.buildException("菜单类型时，路由地址不能为空");
        }
        if (menuModel.getMenuType() == AppConstants.ONE && Objects.isNull(menuModel.getPermissions())) {
            throw DataErrorCode.DATA_VALIDATION_FAILED.buildException("按钮类型时，权限标识不能为空");
        }

        if (menuModel.getPid() == 0) {
            return null;
        }
        return menuRepository.findById(menuModel.getPid()).orElseThrow(() -> DataErrorCode.DATA_NOT_FOUND.buildException("父级菜单不存在"));
    }

    public String getMenuCode(String menuCode ,String merchantCode ) {
        String code = "";
        if (ObjectUtil.isEmpty(menuCode)) {
            code =  RandomUtil.randomStringUpper(AppConstants.FOUR);
        } else {
            code = menuCode.concat(AppConstants.DASH_CHAR).concat(RandomUtil.randomStringUpper(AppConstants.FOUR));
        }
        if (validatedMenuCodeIsExists(code, merchantCode)) {
            getMenuCode(menuCode,merchantCode);
        }
        return code ;
    }

    /**
     * 验证菜单编码是否存在
     * @param menuCode 菜单编码
     */
    public Boolean validatedMenuCodeIsExists(String menuCode ,String merchantCode) {
        Long num = menuRepository.countByMerchantCodeAndMenuCode(merchantCode, menuCode);
        if (num > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据菜单id获取用户id列表
     * @param menuId 菜单id
     * @return 用户id列表
     */
    public List<Long> getUserIdsByMenuId(Long menuId) {
        QRoleMenu roleMenu = QRoleMenu.roleMenu;
        QRoleUser qRoleUser = QRoleUser.roleUser;
        List<Long> userIdList = jpaQueryFactory.select(qRoleUser.userId)
                .from(qRoleUser)
                .innerJoin(roleMenu).on(qRoleUser.roleId.eq(roleMenu.roleId))
                .where(roleMenu.menuId.eq(menuId)).fetch();
        return userIdList;
    }


    /**
     * 判断菜单是否影响用户 并清除缓存
     * @param menuId 菜单id
     */
    public void isMenuAffectingUserid(Long menuId) {
        // 判断菜单影响的用户
        List<Long> userIds = getUserIdsByMenuId(menuId);
        // 清除缓存
        userIds.forEach(id -> {
            RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.USER_MENU_LIST.getName(),id));
        });
        // 判断菜单是否影响角色
        List<Long> roleIdListByMenuIds = ServiceManager.roleMenuService.getRoleIdListByMenuIds(menuId);
        roleIdListByMenuIds.forEach(roleId -> {
            RedisUtil.delete(RedisUtil.genKey(MenuCacheEnum.ROLE_MENU_LIST.getName(),roleId));
        });

    }
}
