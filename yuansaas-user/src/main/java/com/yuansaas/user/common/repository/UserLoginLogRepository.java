package com.yuansaas.user.common.repository;

import com.yuansaas.user.common.entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户登录日志
 *
 * @author HTB 2025/7/31 16:19
 */
public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}
