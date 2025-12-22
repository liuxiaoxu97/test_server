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
public class UpdateDictParam {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 字典名称
     */
    @NotNull(message = "字典名称不能为空")
    private String dictName;
    /**
     * 字典类型
     */
    @NotNull(message = "字典类型不能为空")
    private String dictType;
    /**
     * 平台类型
     */
    @EnumValidate(enumClass = PlatformTypeEnum.class)
    private PlatformTypeEnum platform;
    /**
     * 字典排序值
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
}
