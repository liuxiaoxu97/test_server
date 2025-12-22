package com.yuansaas.core.exception.ex;

import com.yuansaas.core.exception.IErrorCodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 认证错误码
 *
 * @author HTB 2025/7/28 16:10
 */
@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements IErrorCodeEnum<AuthErrorCode> {

    // ======================== 认证授权错误 ========================
    UNAUTHORIZED("AUTH_0001", "未授权访问"),
    AUTHENTICATION_FAILED("AUTH_0002", "认证失败"),
    INVALID_TOKEN("AUTH_0003", "无效令牌"),
    TOKEN_EXPIRED("AUTH_0004", "令牌已过期"),
    ACCESS_DENIED("AUTH_0005", "访问被拒绝"),
    INSUFFICIENT_PERMISSIONS("AUTH_0006", "权限不足"),
    ACCOUNT_LOCKED("AUTH_0007", "账户已锁定"),
    ACCOUNT_DISABLED("AUTH_0008", "账户已禁用"),
    INVALID_CREDENTIALS("AUTH_0009", "凭证无效"),
    SESSION_EXPIRED("AUTH_0010", "会话已过期"),
    PASSWORD_EXPIRED("AUTH_0011", "密码已过期"),
    PASSWORD_RESET_REQUIRED("AUTH_0012", "需要重置密码"),
    AUTH_PARAM_ERROR("AUTH_0013", "认证参数错误"),
    ;

    private final String code;
    private final String message;
}
