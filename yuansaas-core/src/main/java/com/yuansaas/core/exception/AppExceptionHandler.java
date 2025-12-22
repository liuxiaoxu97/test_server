package com.yuansaas.core.exception;

import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常
 *
 * @author HTB 2025/7/21 22:31
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler extends BaseExceptionHandler{

    /**
     * 处理业务异常
     * @param ex 业务异常
     * @param request 请求
     * @return  ResponseEntity
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseModel<Object>> handleAppException(AppException ex, HttpServletRequest request) {
        logException(ex, request);
        ResponseModel<Object> response = ResponseModel.<Object>builder()
                .status("ERROR")
                .code(ex.getCode())
                .message(ex.getMessage())
                .data(ex.getErrorData())
                .traceId(AppContextUtil.requireTraceId())
                .build();
        if(super.isOutputStackTrace()){
            response.setDebug(getStackTraceAsString(ex));
        }
         return ResponseBuilder.toResponseEntity( response , HttpStatus.OK);
    }

    /**
     * 记录异常日志
     */
    private void logException(AppException ex, HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getHeader("X-HTTP-Method-Override");
        if (method == null) {
            method = request.getMethod();
        }

        if (ex.isShowStackTrace()) {
            log.error("业务异常 [{}]: {} {} - {}",
                    ex.getCode(), method, path, ex.getMessage(), ex);
        } else {
            log.warn("业务异常 [{}]: {} {} - {}",
                    ex.getCode(), method, path, ex.getMessage());
        }

    }
}
