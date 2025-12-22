package com.yuansaas.user.dept.params;

import com.yuansaas.core.page.PageModel;
import lombok.Data;

/**
 *
 * 查询部门信息参数
 *
 * @author LXZ 2025/10/17 11:46
 */
@Data
public class FindDeptParam {
    /**
     * 商家code
     */
    private String merchantCode;
    /**
     * 部门名称
     */
    private String deptName;
}
