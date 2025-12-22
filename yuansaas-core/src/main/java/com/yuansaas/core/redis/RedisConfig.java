package com.yuansaas.core.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 配置
 *
 * @author HTB 2025/8/12 18:38
 */
@Configuration
public class RedisConfig {

    /**
     * 配置 StringRedisTemplate，方便操作 Redis 字符串类型缓存
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

}
