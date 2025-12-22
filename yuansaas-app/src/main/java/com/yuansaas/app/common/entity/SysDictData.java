package com.yuansaas.app.common.entity;

import com.yuansaas.app.common.enums.PlatformTypeEnum;
import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * 字典数据
 *
 * @author LXZ 2025/11/17 15:46
 */
@Data
@Entity
@Table(name = "sys_dict_data")
public class SysDictData extends BaseEntity {

    /**
     * 字典类型ID
     */
    private  Long  dictTypeId;
    /**
     * 字典标签
     */
    private  String  dictLabel;
    /**
     * 字典键值
     */
    private  String  dictValue;
    /**
     * 平台类型
     */
    private PlatformTypeEnum platform;
    /**
     * 字典排序
     */
    private  Integer sort;
}
