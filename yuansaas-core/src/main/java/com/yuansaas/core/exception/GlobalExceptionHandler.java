package com.yuansaas.core.exception;

import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author HTB 2025/7/21 18:15
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler{

    /**
     * 404 资源未找到异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseModel<Void>> handleNoHandlerFound(
            NoHandlerFoundException ex, HttpServletRequest request) {

        log.error("404 Not Found: {} {}", request.getMethod(),  request.getRequestURI());

        return ResponseBuilder.failResponse("请求资源不存在" , HttpStatus.NOT_FOUND);
    }

    /**
     * 请求方法不支持异常 (405)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseModel<Void>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        String supportedMethods = String.join(", ", Objects.requireNonNull(ex.getSupportedMethods()));
        String message = String.format("请求方法不支持，支持的请求方法: %s", supportedMethods);
        log.error("405 : {}", message);
        return ResponseBuilder.failResponse("请求方法不支持" ,HttpStatus.METHOD_NOT_ALLOWED );
    }

    /**
     * 参数绑定异常 (400)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseModel<Map<String, String>>> handleBindException(BindException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1 + "; " + msg2
                ));

        String summary = errors.size() == 1 ?
                "参数绑定失败: " + errors.values().iterator().next() :
                String.format("参数绑定失败: %d 个字段存在问题", errors.size());

        log.error("400: 参数绑定失败: {}", summary);
        return ResponseBuilder.errorResponse("参数绑定失败" , HttpStatus.BAD_REQUEST ,  errors);

    }

    /**
     * 方法参数验证异常 (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel<Map<String , String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1 + "; " + msg2
                ));

        String summary = errors.size() == 1 ?
                "参数验证失败: " + errors.values().iterator().next() :
                String.format("参数验证失败: %d 个字段存在问题", errors.size());

        log.error("400: 参数验证失败: {}", summary);
        return ResponseBuilder.errorResponse("参数验证失败" , HttpStatus.BAD_REQUEST ,  errors);
    }

    /**
     * 方法参数验证异常 (400)
     */
    @ExceptionHandler( MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseModel<Map<String , String>>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String message = String.format("参数类型错误: 参数 '%s' 需要类型 %s, 但实际值为 '%s'",
                ex.getName(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName(),
                ex.getValue());
        log.error("400: 参数类型错误: {}", message);
        return ResponseBuilder.errorResponse("参数类型错误" , HttpStatus.BAD_REQUEST );
    }

    /**
     * 请求参数缺失异常 (400)
     */
    @ExceptionHandler(MissingServletRequestParameterException .class)
    public ResponseEntity<ResponseModel<Void>> handleMissingParameter(
            MissingServletRequestParameterException ex) {

        log.error("400: 缺少必要参数: {} 类型: {}", ex.getParameterName(), ex.getParameterType());
        return ResponseBuilder.errorResponse("缺少必要参数" ,HttpStatus.BAD_REQUEST);
    }

    /**
     * JSON解析异常 (400)
     */
    @ExceptionHandler( HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseModel<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        String rootCause = ex.getRootCause() != null ?
                ex.getRootCause().getMessage() : ex.getMessage();

        log.error("400: 请求体解析失败，请检查JSON格式: {}", rootCause);
        return ResponseBuilder.errorResponse("请求体解析失败，请检查JSON格式" ,HttpStatus.BAD_REQUEST);
    }

    /**
     * 所有未处理的异常 (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel<Void>> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.error("系统异常: {} {} - {}", method, path, ex.getMessage(), ex);
        ResponseModel<Void> responseModel = ResponseBuilder.fail("系统繁忙，请稍后重试");
        if(super.isOutputStackTrace()){
            responseModel.setDebug(getStackTraceAsString(ex));
        }
        return ResponseBuilder.toResponseEntity(responseModel , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
