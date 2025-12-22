package com.yuansaas.app.common.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 修改排序值
 *
 * @author LXZ 2025/11/18 18:44
 */
@Data
public class UpdateSortParam implements Serializable {
    /**
     * 字典id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 排序值
     */
    @NotNull(message = "排序值不能为空")
    private Integer sort;
}
