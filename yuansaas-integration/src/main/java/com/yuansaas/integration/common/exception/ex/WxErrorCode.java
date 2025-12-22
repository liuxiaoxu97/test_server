package com.yuansaas.integration.common.exception.ex;

import com.yuansaas.core.exception.IErrorCodeEnum;
import com.yuansaas.core.exception.ex.DataErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 微信异常code
 *
 * @author HTB 2025/8/21 17:44
 */
@Getter
@RequiredArgsConstructor
public enum WxErrorCode implements IErrorCodeEnum<DataErrorCode> {
    // ----------------- 微信公众号错误码 -----------------
    WX_MP_SIGNATURE_ERROR("WX_MP_0001", "公众号签名验证失败"),;

    private final String  code;

    private final String message;

}
