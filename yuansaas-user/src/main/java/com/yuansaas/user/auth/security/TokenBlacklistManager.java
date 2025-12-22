package com.yuansaas.user.auth.security;

import com.yuansaas.core.redis.RedisUtil;
import com.yuansaas.user.common.enums.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * token 黑明单管理
 *
 * @author HTB 2025/8/11 14:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenBlacklistManager {

    private final JwtTokenParser jwtTokenParser;

    // 黑名单单个令牌
    public void blackListToken(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }

        try {
            if (!jwtTokenParser.isTokenValid(token)) {
                // 令牌已失效
                log.debug("令牌无效，跳过黑名单: {}", token);
                return;
            }
            // 获取令牌过期时间
            Date expiration = jwtTokenParser.extractExpiration(token);
            if (expiration == null) {
                log.warn("无法获取令牌过期时间: {}", token);
                return;
            }

            // 计算剩余有效时间（秒）
            long currentTimeMillis = System.currentTimeMillis();
            long expirationTimeMillis = expiration.getTime();
            long ttlSeconds = (expirationTimeMillis - currentTimeMillis) ;

            if (ttlSeconds > 0) {
                // 存储令牌黑名单
                String key = buildTokenKey(token);
                RedisUtil.set(key, "invalid", ttlSeconds, TimeUnit.SECONDS);
                log.debug("令牌加入黑名单: {}", token);
            }
        } catch (Exception e) {
            log.error("加入令牌黑名单失败: {}", token, e);
        }
    }

    // 黑名单用户的所有令牌
    public void blacklistUserTokens(Long userId, UserType userType) {
        if (userId == null || userType == null) {
            return;
        }

        try {
            // 设置用户黑名单标志
            String userKey = buildUserKey(userId, userType);

            // 使用刷新令牌的有效期作为黑名单有效期
            long ttlSeconds = jwtTokenParser.extractRefreshExpiration() / 1000;
            RedisUtil.set(userKey, "invalid", ttlSeconds, TimeUnit.SECONDS);
            log.info("用户所有令牌加入黑名单: userId={}, userType={}", userId, userType);
        } catch (Exception e) {
            log.error("加入用户令牌黑名单失败: userId={}, userType={}", userId, userType, e);
        }
    }

    // 解除用户黑名单状态
    public void unBlackListUser(Long userId, UserType userType) {
        if (userId == null || userType == null) {
            return;
        }

        try {
            String userKey = buildUserKey(userId, userType);
            RedisUtil.delete(userKey);
            log.info("用户解除黑名单: userId={}, userType={}", userId, userType);
        } catch (Exception e) {
            log.error("解除用户黑名单失败: userId={}, userType={}", userId, userType, e);
        }
    }

    // 检查令牌是否在黑名单中
    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            String key = buildTokenKey(token);
            return Boolean.TRUE.equals(RedisUtil.hasKey(key));
        } catch (Exception e) {
            log.error("检查令牌黑名单失败: {}", token, e);
            return false;
        }
    }

    // 检查用户是否在黑名单中
    public boolean isUserBlacklisted(Long userId, UserType userType) {
        if (userId == null || userType == null) {
            return false;
        }

        try {
            String userKey = buildUserKey(userId, userType);
            return Boolean.TRUE.equals(RedisUtil.hasKey(userKey));
        } catch (Exception e) {
            log.error("检查用户黑名单失败: userId={}, userType={}", userId, userType, e);
            return false;
        }
    }

    // 构建令牌黑名单键
    private String buildTokenKey(String token) {
        return RedisUtil.genKey("blacklist", "token", token);
    }

    // 构建用户黑名单键
    private String buildUserKey(Long userId, UserType userType) {
        return RedisUtil.genKey("blacklist", "user", userType.name(), userId);
    }

}
