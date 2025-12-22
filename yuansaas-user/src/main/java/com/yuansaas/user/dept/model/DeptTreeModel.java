package com.yuansaas.user.dept.model;

import com.yuansaas.user.dept.vo.DeptTreeListVo;
import lombok.Data;

import java.util.List;

/**
 *
 * 部门树结构model
 *
 * @author LXZ 2025/10/17 20:48
 */
@Data
public class DeptTreeModel {
    /**
     * 部门id
     */
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 子部门
     */
    private List<DeptTreeModel> childrenDeptList;
    /**
     * 是否启用
     */
    private String lockStatus;
}
