package com.yuansaas.user.auth.model;

import com.yuansaas.user.common.enums.UserType;
import lombok.Data;

/**
 * 锁定用户事件
 *
 * @author HTB 2025/8/11 12:29
 */
@Data
public class UserUnlockEvent {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户类型
     */
    private UserType userType;

    public UserUnlockEvent(Long userId, UserType userType) {
        this.userId = userId;
        this.userType = userType;
    }
}
