package com.yuansaas.core.jpa.hibernate;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Component;

/**
 * hibernate配置 注册Json类型
 *
 * @author HTB 2025/8/22 12:21
 */
@Component
public class JsonTypeContributor implements TypeContributor {

    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        // 注册自定义类型
        typeContributions.getTypeConfiguration()
                .getBasicTypeRegistry()
                .register(JsonType.INSTANCE , "json");
    }
}
