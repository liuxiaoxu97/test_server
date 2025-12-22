package com.yuansaas.user.dept.params;

import lombok.Data;

/**
 *
 * 创建部门
 *
 * @author LXZ 2025/10/17 11:51
 */
@Data
public class UpdateDeptParam {

    /**
     * 部门id
     */
    private Long id;
    /**
     * 部门名称
     */
    private String name;
}
