package com.yuansaas.app.common.repository;

import com.yuansaas.app.common.entity.ThirdPartyMessageParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 第三方消息参数
 *
 * @author HTB 2025/8/22 14:45
 */
public interface ThirdPartyMessageParamsRepository extends JpaRepository<ThirdPartyMessageParams, Long> {
    /**
     * 根据配置键名查找配置
     */
    Optional<ThirdPartyMessageParams> findByConfigKey(String configKey);

    /**
     * 增加使用次数
     */
    @Modifying
    @Query(value = "UPDATE third_party_message_params c SET c.usage_count = c.usage_count + 1, c.updatedAt = :updatedAt , c.updatedBy = :updatedBy WHERE c.id = :id " , nativeQuery = true)
    void incrementUsageCount(@Param("id") Long id , @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 禁用配置
     */
    @Modifying
    @Query(value = "UPDATE third_party_message_params c SET c.status = 0, c.updatedAt = :updatedAt , c.updatedBy = :updatedBy WHERE c.config_key = :configKey " , nativeQuery = true)
    void disableConfig(@Param("configKey") String configKey , @Param("updatedAt") LocalDateTime updatedAt , @Param("updatedBy") String updatedBy);
}
