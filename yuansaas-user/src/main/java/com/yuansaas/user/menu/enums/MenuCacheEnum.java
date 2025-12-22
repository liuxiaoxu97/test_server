package com.yuansaas.user.menu.enums;

import com.yuansaas.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 菜单缓存枚举
 *
 * @author LXZ 2025/10/17 18:11
 */
@Getter
@AllArgsConstructor
public enum MenuCacheEnum implements IBaseEnum<MenuCacheEnum> {

    MENU_LIST("MENU_LIST", "菜单列表缓存"),

    USER_MENU_LIST("USER_MENU_LIST", "用户菜单列表缓存"),

    ROLE_MENU_LIST("ROLE_MENU_LIST", "角色菜单列表缓存"),
    ;

    private final String key;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }
}
