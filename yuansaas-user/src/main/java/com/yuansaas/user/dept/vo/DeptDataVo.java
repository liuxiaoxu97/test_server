package com.yuansaas.user.dept.vo;

import lombok.Data;

import java.util.Date;

/**
 *
 * 部门详情
 *
 * @author LXZ 2025/10/16 17:00
 */
@Data
public class DeptDataVo {
    /**
     * 部门id
     */
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 父级id
     */
    private Long pid;
    /**
     * 是否启用
     */
    private String lockStatus;
    /**
     * 删除状态
     */
    private String deleteStatus;
}
