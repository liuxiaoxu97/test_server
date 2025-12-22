package com.yuansaas.core.jpa.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

/**
 * 基础的Entity
 *
 * @author HTB 2025/7/31 15:17
 */
@Setter
@Getter
@MappedSuperclass
public class BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GenericGenerator(
            name = "id",
            strategy = "com.yuansaas.core.jpa.id.CustomIdentityGenerator"
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "id"
    )
    private Long id;
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

    /**
     * 备注
     */
    private String remark;

}
