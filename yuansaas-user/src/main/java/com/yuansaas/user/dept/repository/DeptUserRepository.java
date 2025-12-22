package com.yuansaas.user.dept.repository;

import com.yuansaas.user.dept.entity.SysDeptUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 *
 * @author LXZ 2025/10/27 16:15
 */
public interface DeptUserRepository extends JpaRepository<SysDeptUser, Long> {

    void deleteByDeptIdAndUserId(Long deptId , Long userId);

    void deleteByDeptId(Long deptId);

    void deleteByUserId(Long userId);

    List<SysDeptUser> findByDeptId(Long deptId);

    SysDeptUser findByUserId(Long userId);
}
