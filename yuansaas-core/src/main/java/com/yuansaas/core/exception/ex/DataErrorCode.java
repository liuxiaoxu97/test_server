package com.yuansaas.core.exception.ex;

import com.yuansaas.core.exception.IErrorCodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据相关异常码
 *
 * @author HTB 2025/7/28 16:07
 */
@Getter
@RequiredArgsConstructor
public enum DataErrorCode implements IErrorCodeEnum<DataErrorCode> {

    // ======================== 数据相关错误 ========================
    DATA_NOT_FOUND("DATA_0001", "数据未找到"),
    DATA_ALREADY_EXISTS("DATA_0002", "数据已存在"),
    DATA_VALIDATION_FAILED("DATA_0003", "数据验证失败"),
    DATA_INTEGRITY_VIOLATION("DATA_0004", "数据完整性冲突"),
    DATA_VERSION_CONFLICT("DATA_0005", "数据版本冲突"),
    DATA_FORMAT_ERROR("DATA_0006", "数据格式错误"),
    DATA_PERMISSION_DENIED("DATA_0007", "数据访问权限不足"),
    DATA_DELETED("DATA_0008", "数据已被删除"),
    DATA_IMPORT_ERROR("DATA_0009", "数据导入失败"),
    DATA_EXPORT_ERROR("DATA_0010", "数据导出失败"),
    DATA_DISABLED("DATA_0010", "数据已禁用"),

    ;

    private final String  code;

    private final String message;
}
