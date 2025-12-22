package com.yuansaas.user.dept.enums;

import com.yuansaas.common.enums.IBaseEnum;
import com.yuansaas.user.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 部门缓存枚举
 *
 * @author LXZ 2025/10/17 18:11
 */
@Getter
@AllArgsConstructor
public enum DeptCacheEnum implements IBaseEnum<DeptCacheEnum> {

    DEPT_TREE_LIST("dept_tree_list", "部门树状列表缓存"),
    ;

    private final String key;
    private final String desc;

    @Override
    public String getName() {
        return this.name();
    }
}
