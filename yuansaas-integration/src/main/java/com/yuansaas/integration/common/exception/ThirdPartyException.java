package com.yuansaas.integration.common.exception;

/**
 * 第三方异常
 *
 * @author HTB 2025/8/13 16:39
 */
public class ThirdPartyException extends RuntimeException {
    public ThirdPartyException(String message) {
        super(message);
    }
}
