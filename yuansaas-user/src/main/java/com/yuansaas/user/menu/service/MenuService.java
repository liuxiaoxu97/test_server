package com.yuansaas.user.menu.service;

import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import com.yuansaas.user.menu.entity.Menu;
import com.yuansaas.user.menu.params.FindMenuParam;
import com.yuansaas.user.menu.params.SaveMenuParam;
import com.yuansaas.user.menu.params.UpdateMenuParam;
import com.yuansaas.user.menu.vo.MenuListVo;
import com.yuansaas.user.menu.vo.MenuVo;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 菜单服务
 * @author LXZ 2025/10/21 11:48
 */
public interface MenuService {

    /**
     * 列表查询
     *
     */
    List<MenuListVo> list(FindMenuParam findMenuParam);

    /**
     * 新增菜单
     */
    Boolean save(SaveMenuParam saveMenuParam);
    /**
     * 修改菜单
     */
    Boolean update(UpdateMenuParam updateMenuParam);
    /**
     * 删除菜单
     */
    Boolean delete( Long id );
    /**
     * 禁用菜单
     */
    Boolean lock( Long id );
    /**
     * 菜单详情
     */
    MenuVo getById(Long id);
    /**
     * 批量获取菜单 (原始信息)
     */
    List<Menu> getByList(List<Long> ids , String lockStatus);
}
