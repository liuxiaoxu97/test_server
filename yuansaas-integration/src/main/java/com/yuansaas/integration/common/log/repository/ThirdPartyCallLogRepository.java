package com.yuansaas.integration.common.log.repository;

import com.yuansaas.integration.common.log.entity.ThirdPartyCallLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 第三方调用
 *
 * @author HTB 2025/8/13 17:00
 */
public interface ThirdPartyCallLogRepository extends JpaRepository<ThirdPartyCallLog, Long> {

}
