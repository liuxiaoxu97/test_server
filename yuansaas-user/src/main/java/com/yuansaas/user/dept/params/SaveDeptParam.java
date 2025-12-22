package com.yuansaas.user.dept.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * 创建部门
 *
 * @author LXZ 2025/10/17 11:51
 */
@Data
public class SaveDeptParam {
    /**
     * 商户编号
     */
    @NotNull(message = "商户编号不能为空")
    private String merchantCode;
    /**
     * 父部门ID
     */
    @NotNull(message = "父部门ID不能为空")
    private Long pid;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String name;
}
