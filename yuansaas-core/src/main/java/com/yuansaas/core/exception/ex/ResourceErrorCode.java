package com.yuansaas.core.exception.ex;

import com.yuansaas.core.exception.IErrorCodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资源异常码
 *
 * @author HTB 2025/7/28 16:13
 */

@Getter
@RequiredArgsConstructor
public enum ResourceErrorCode implements IErrorCodeEnum<ResourceErrorCode> {

    // ======================== 资源相关错误 ========================
    RESOURCE_NOT_FOUND("RES_0001", "资源未找到"),
    RESOURCE_UNAVAILABLE("RES_0002", "资源不可用"),
    RESOURCE_LIMIT_EXCEEDED("RES_0003", "资源超限"),
    RESOURCE_CONFLICT("RES_0004", "资源冲突"),
    RESOURCE_PERMISSION_DENIED("RES_0005", "资源访问权限不足"),
    RESOURCE_DELETED("RES_0006", "资源已被删除"),
    RESOURCE_EXPIRED("RES_0007", "资源已过期"),
    RESOURCE_LOCKED("RES_0008", "资源已被锁定"),

    ;

    private final String code;
    private final String message;
}
