package com.yuansaas.user.system.repository;

import com.yuansaas.user.system.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 系统用户
 *
 * @author HTB 2025/7/31 16:22
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByUserName(String username);
}
