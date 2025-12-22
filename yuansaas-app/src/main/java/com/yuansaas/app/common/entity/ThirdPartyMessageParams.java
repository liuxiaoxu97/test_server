package com.yuansaas.app.common.entity;

import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.jpa.model.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 第三方消息参数记录
 *
 * @author HTB 2025/8/22 11:41
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "third_party_message_params")
@Data
public class ThirdPartyMessageParams extends BaseEntity {

    /**
     * 配置键名（唯一业务标识）
     */
    private String configKey;

    /**
     * 消息场景（sms/email/wechat等）
     */
    private String scene;

    /**
     * 状态(0-禁用,1-启用
     */
    private Boolean status;

    /**
     * 参数配置（JSON字符串）
     */
    @Type(value = JsonStringType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> params;

    /**
     * 使用次数
     */
    private Integer usageCount = 0;

    /**
     * 增加使用次数
     */
    public void incrementUsageCount() {
        this.usageCount++;
        super.setUpdateAt(LocalDateTime.now());
        super.setUpdateBy(AppContextUtil.getUserInfo());
    }

}
