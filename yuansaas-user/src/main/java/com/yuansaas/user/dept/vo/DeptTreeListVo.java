package com.yuansaas.user.dept.vo;

import com.yuansaas.core.model.TreeNode;
import com.yuansaas.user.dept.model.DeptTreeModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * 部门树列表vo
 *
 * @author LXZ 2025/10/16 16:44
 */
@Data
public class DeptTreeListVo extends TreeNode {

    /**
     * 部门id
     */
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 是否启用
     */
    private String lockStatus;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createAt;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateAt;

}
