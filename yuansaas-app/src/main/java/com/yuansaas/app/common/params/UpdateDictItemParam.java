package com.yuansaas.app.common.params;


import com.yuansaas.app.common.enums.PlatformTypeEnum;
import com.yuansaas.core.annotation.EnumValidate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典数据添加参数
 *
 * @author Yang 2021-05-17 09:41
 */
@Data
public class UpdateDictItemParam {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 字典key
     */
    @NotNull(message = "字典key不能为空")
    private String dictLabel;
    /**
     * 字典value
     */
    @NotNull(message = "字典value不能为空")
    private String dictValue;
    /**
     * 字典排序值
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
}
