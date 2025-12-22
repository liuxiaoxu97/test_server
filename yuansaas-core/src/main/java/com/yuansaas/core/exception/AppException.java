package com.yuansaas.core.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 *
 * @author HTB 2025/7/21 21:43
 */

@Getter
public class AppException extends RuntimeException{

    /**
     * 错误码
     */
    private final String code ;

    /**
     * 错误数据
     */
    private final Object errorData;

    /**
     * 是否显示堆栈信息
     */
    private final boolean showStackTrace;

    public AppException(String code, String message) {
        super(message);
        this.code = code;
        this.showStackTrace = false;
        this.errorData = null;
    }

    public AppException(String code, String userMessage, Object errorData) {
        super(userMessage);
        this.code = code;
        this.errorData = errorData;
        this.showStackTrace = false;
    }

    public AppException(String code, String message , Object errorData , boolean showStackTrace) {
        super(message);
        this.code = code;
        this.errorData = errorData;
        this.showStackTrace = showStackTrace;
    }

    /**
     * 流式构建方法
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 异常构建器
     */
    public static class Builder {
        private String code = "0000";
        private String message;
        private Object errorData;
        private boolean logStackTrace;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errorData(Object errorData) {
            this.errorData = errorData;
            return this;
        }

        public Builder logStackTrace(boolean logStackTrace) {
            this.logStackTrace = logStackTrace;
            return this;
        }

        public AppException build() {
            return new AppException(code, message, errorData, logStackTrace);
        }
    }

}
