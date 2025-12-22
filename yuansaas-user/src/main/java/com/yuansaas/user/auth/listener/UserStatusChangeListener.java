package com.yuansaas.user.auth.listener;

import com.yuansaas.user.auth.model.UserLockEvent;
import com.yuansaas.user.auth.model.UserUnlockEvent;
import com.yuansaas.user.auth.security.TokenBlacklistManager;
import com.yuansaas.user.common.service.UserStatusCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 用户状态变更事件监听器
 *
 * @author HTB 2025/8/11 14:50
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserStatusChangeListener {
    private final TokenBlacklistManager tokenBlacklistManager;
    private final UserStatusCache userStatusCache;

    @EventListener
    public void handleUserLockedEvent(UserLockEvent event) {
        log.info("处理用户锁定事件: userId={}, userType={}", event.getUserId(), event.getUserType());

        // 1. 使该用户的所有令牌失效
        tokenBlacklistManager.blacklistUserTokens(event.getUserId(), event.getUserType());

        // 2. 清除用户状态缓存
        userStatusCache.evictUser(event.getUserId(), event.getUserType());
    }

    @EventListener
    public void handleUserUnlockedEvent(UserUnlockEvent event) {
        log.info("处理用户解锁事件: userId={}, userType={}", event.getUserId(), event.getUserType());

        // 1. 清除用户黑名单状态
        tokenBlacklistManager.unBlackListUser(event.getUserId(), event.getUserType());

        // 2. 清除用户状态缓存
        userStatusCache.evictUser(event.getUserId(), event.getUserType());
    }

}
