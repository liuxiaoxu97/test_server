package com.yuansaas.core.exception.ex;

import com.yuansaas.core.exception.IErrorCodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 业务异常码
 *
 * @author HTB 2025/7/28 16:12
 */
@Getter
@RequiredArgsConstructor
public enum BizErrorCode implements IErrorCodeEnum<BizErrorCode> {

    // ======================== 业务逻辑错误 ========================
    BUSINESS_RULE_VIOLATION("BIZ_0001", "违反业务规则"),
    BUSINESS_LIMIT_EXCEEDED("BIZ_0002", "业务限制超限"),
    BUSINESS_STATE_INVALID("BIZ_0003", "业务状态无效"),
    BUSINESS_PROCESS_FAILED("BIZ_0004", "业务流程执行失败"),
    BUSINESS_CONFLICT("BIZ_0005", "业务冲突"),
    BUSINESS_NOT_SUPPORTED("BIZ_0006", "业务操作不支持"),
    BUSINESS_TIMEOUT("BIZ_0007", "业务处理超时"),
    BUSINESS_RESOURCE_UNAVAILABLE("BIZ_0008", "业务资源不可用"),
    BUSINESS_CONDITION_NOT_MET("BIZ_0009", "业务条件不满足"),
    BUSINESS_OPERATION_DUPLICATE("BIZ_0010", "重复的业务操作"),
    BUSINESS_VALIDATION_FAILED("BIZ_0011", "业务验证失败"),
    BUSINESS_OPERATION_LIMIT("BIZ_00012", "业务操作次数限制"),
    BUSINESS_OPERATION_NOT_ALLOWED("BIZ_00013", "业务操作不允许"),
    BUSINESS_OPERATION_FAILED("BIZ_00014", "业务操作失败"),

    ;

    private final String code;
    private final String message;
}
