package com.yuansaas.core.jpa.id;

import com.yuansaas.core.utils.id.SnowflakeIdGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 雪花算法ID生成器（带ID存在检查）
 *
 * 特性：
 * 1. 如果实体已设置ID，则保留原ID
 * 2. 未设置ID时自动生成雪花ID
 * 3. 支持所有实体类型
 */

public class CustomIdentityGenerator implements IdentifierGenerator, ApplicationContextAware {

    private static SnowflakeIdGenerator idWorker;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object entity)
            throws HibernateException {

        // 检查是否已存在ID
        Serializable existingId = getExistingId(entity);
        if (existingId != null) {
            return existingId;
        }

        // 生成新ID
        return idWorker.nextId();
    }

    /**
     * 通过反射获取实体已存在的ID
     */
    private Serializable getExistingId(Object entity) {
        try {
            // 查找被@Id注解的字段
            for (Field field : entity.getClass().getDeclaredFields()) {

                if (field.isAnnotationPresent(jakarta.persistence.Id.class)) {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (value != null) {
                        return (Serializable) value;
                    }
                }
            }

            // 查找getId()方法
            try {
                Object id = entity.getClass()
                        .getMethod("getId")
                        .invoke(entity);
                if (id != null) {
                    return (Serializable) id;
                }
            } catch (NoSuchMethodException e) {
                // 忽略没有getId方法的情况
            }

            return null;
        } catch (Exception e) {
            throw new HibernateException("获取实体ID失败", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (idWorker == null) {
            idWorker = applicationContext.getBean(SnowflakeIdGenerator.class);
        }
    }
}
