package com.yuansaas.user.auth.security;

import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.BaseExceptionHandler;
import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 安全认证异常处理
 *
 * @author HTB 2025/8/12 16:02
 */
@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理安全认证异常
     * @param ex 安全认证异常
     * @return 响应实体
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseModel<Objects>> handleAccessDenied(AccessDeniedException ex) {

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String path = attributes != null ? attributes.getRequest().getRequestURI() : "未知路径";

        log.warn("访问拒绝: 用户 {} 尝试访问 {} 需要额外权限", AppContextUtil.getUserInfo(), path);

        return ResponseBuilder.errorResponse("权限不足" , HttpStatus.UNAUTHORIZED);

    }
}
