package com.yuansaas.core.exception;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 基础的异常类
 *
 * @author HTB 2025/7/21 22:40
 */
@Data
public class BaseExceptionHandler {

    @Value("${ev.app.exception.stack-trace:false}")
    private boolean outputStackTrace;

    /**
     * 获取异常的堆栈信息字符串
     */
    public String getStackTraceAsString(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\n");

        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
        }

        return sb.toString();
    }

}
