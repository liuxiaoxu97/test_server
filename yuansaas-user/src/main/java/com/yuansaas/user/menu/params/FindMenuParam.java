package com.yuansaas.user.menu.params;

import lombok.Data;

/**
 *
 * 查询菜单列表参数
 *
 * @author LXZ 2025/10/21 11:48
 */
@Data
public class FindMenuParam {

    private String merchantCode;

    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer menuType;
}
