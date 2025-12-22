package com.yuansaas.core.exception.ex;

import com.yuansaas.core.exception.IErrorCodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统异常码
 *
 * @author HTB 2025/7/28 16:05
 */
@RequiredArgsConstructor
@Getter
public enum SysErrorCode implements IErrorCodeEnum<SysErrorCode> {

    // ======================== 系统级错误 ========================
    SYSTEM_ERROR("SYS_0001", "系统内部错误"),
    SERVICE_UNAVAILABLE("SYS_0002", "服务不可用"),
    SERVICE_TIMEOUT("SYS_0003", "服务调用超时"),
    DATABASE_ERROR("SYS_0004", "数据库操作失败"),
    NETWORK_ERROR("SYS_0005", "网络错误"),
    CONFIGURATION_ERROR("SYS_0006", "配置错误"),
    THIRD_PARTY_SERVICE_ERROR("SYS_0007", "第三方服务异常"),
    API_RATE_LIMIT_EXCEEDED("SYS_0008", "API调用频率超限"),
    FILE_OPERATION_ERROR("SYS_0009", "文件操作失败"),

    ;

    private final String code;

    private final String message;
}
