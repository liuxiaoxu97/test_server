package com.yuansaas.core.exception.ex;

import com.yuansaas.core.exception.IErrorCodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 参数异常码
 *
 * @author HTB 2025/7/28 16:08
 */
@Getter
@RequiredArgsConstructor
public enum ParamErrorCode implements IErrorCodeEnum<ParamErrorCode> {

    // ======================== 参数校验错误 ========================
    PARAMETER_REQUIRED("PARAM_0001", "参数缺失"),
    PARAMETER_INVALID("PARAM_0002", "参数无效"),
    PARAMETER_TYPE_MISMATCH("PARAM_0003", "参数类型不匹配"),
    PARAMETER_FORMAT_ERROR("PARAM_0004", "参数格式错误"),
    PARAMETER_OUT_OF_RANGE("PARAM_0005", "参数超出范围"),
    PARAMETER_LENGTH_EXCEEDED("PARAM_0006", "参数长度超限"),
    PARAMETER_DUPLICATE("PARAM_0007", "参数重复"),
    PARAMETER_ENCRYPTION_ERROR("PARAM_0008", "参数加密失败"),
    PARAMETER_DECRYPTION_ERROR("PARAM_0009", "参数解密失败"),

    ;

    private final String code;

    private final String message;

}
