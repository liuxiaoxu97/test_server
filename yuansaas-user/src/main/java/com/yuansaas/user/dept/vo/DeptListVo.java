package com.yuansaas.user.dept.vo;

import lombok.Data;

import java.util.Date;

/**
 *
 * 部门列表vo
 *
 * @author LXZ 2025/10/16 16:44
 */
@Data
public class DeptListVo {

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
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 修改时间
     */
    private Date updateAt;
}
