package com.yuansaas.user.dept.entity;

import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

/**
 *
 * 部门用户管理
 *
 * @author LXZ 2025/10/16 16:53
 */
@Data
@Entity
@Table(name = "sys_dept_user")
public class SysDeptUser {
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
     * 部门ID
     */
    private Long deptId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createAt;
}
