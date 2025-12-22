package com.yuansaas.core.exception;

/**
 * 错误枚举类
 *
 * @author HTB 2025/7/28 10:32
 */
public interface IErrorCodeEnum<E extends Enum<E>> {

    /**
     * 错误码
     */
    String getCode();

    /**
     * 错误信息
     */
    String getMessage();

//    /**
//     * 获取用户提示信息（默认与错误信息相同）
//     */
//    default String getUserMessage() {
//        return getMessage();
//    }

    /**
     * 快速构建 AppException
     */
    default AppException buildException() {
        return AppException.builder()
                .code(getCode())
                .message(getMessage())
                .logStackTrace(false)
                .build();
    }

    /**
     * 构建带自定义消息的异常
     */
    default AppException buildException(String customMessage) {
        return AppException.builder()
                .code(getCode())
                .message(customMessage)
                .logStackTrace(false)
                .build();
    }

    /**
     * 构建带自定义消息的异常
     */
    default AppException buildException(String customMessage , Object errorData) {
        return AppException.builder()
                .code(getCode())
                .message(customMessage)
                .logStackTrace(false)
                .errorData(errorData)
                .build();
    }


    /**
     * 构建带错误数据的异常
     */
    default AppException buildExceptionWithErrorData(Object errorData) {
        return AppException.builder()
                .code(getCode())
                .message(getMessage())
                .errorData(errorData)
                .logStackTrace(false)
                .build();
    }

    /**
     * 构建带堆栈信息的异常
     */
    default AppException buildExceptionWithStackTrace() {
        return AppException.builder()
                .code(getCode())
                .message(getMessage())
                .logStackTrace(true)
                .build();
    }

    /**
     * 构建带堆栈信息的异常
     */
    default AppException buildExceptionWithStackTrace(String customMessage) {
        return AppException.builder()
                .code(getCode())
                .message(customMessage)
                .logStackTrace(true)
                .build();
    }

    /**
     * 构建带堆栈信息的异常
     */
    default AppException buildExceptionWithStackTrace(String customMessage , Object errorData) {
        return AppException.builder()
                .code(getCode())
                .message(customMessage)
                .errorData(errorData)
                .logStackTrace(true)
                .build();
    }

    /**
     * 检查是否匹配指定错误码
     */
    default boolean matches(String code) {
        return this.getCode().equals(code) ;
    }

    /**
     * 检查是否匹配指定枚举
     */
    default boolean matches(IErrorCodeEnum other) {
        return this == other;
    }

    /**
     * 根据错误码查找枚举
     */
    static <E extends Enum<E> & IErrorCodeEnum> E fromCode(String code, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
