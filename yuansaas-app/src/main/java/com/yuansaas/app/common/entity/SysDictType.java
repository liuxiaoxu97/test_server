package com.yuansaas.app.common.entity;

import com.yuansaas.app.common.enums.PlatformTypeEnum;
import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * 字典类型
 *
 * @author LXZ 2025/11/17 15:51
 */
@Data
@Entity
@Table(name = "sys_dict_type")
public class SysDictType extends BaseEntity {

    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 平台类型
     */
    private PlatformTypeEnum platform;
    /**
     * 排序
     */
    private Integer sort;
}
