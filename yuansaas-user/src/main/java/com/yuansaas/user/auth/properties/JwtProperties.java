package com.yuansaas.user.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt 属性配置类
 *
 * @author HTB 2025/8/11 18:08
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT 密钥（Base64 编码）
     */
    private String secret;

    /**
     * JWT 访问令牌过期时间（毫秒）
     */
    private long expiration;

    /**
     * JWT 刷新令牌过期时间（毫秒）
     */
    private long refreshExpiration;
}
