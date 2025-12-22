package com.yuansaas.core.response;


import com.yuansaas.core.context.AppContext;
import com.yuansaas.core.context.AppContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 响应
 *
 * @author HTB 2025/7/21 17:28
 */
public class ResponseBuilder {

    // ------------ 成功响应 ----------

    public static <T> ResponseModel<T> success() {
        return ResponseModel.<T>builder()
                .status("SUCCESS")
                .code("0000")
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    public static <T> ResponseModel<T> success(T data) {
        return ResponseModel.<T>builder()
                .status("SUCCESS")
                .code("0000")
                .data(data)
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    public static <T> ResponseModel<T> success(String message , T data) {
        return ResponseModel.<T>builder()
                .status("SUCCESS")
                .data(data)
                .code("0000")
                .message(message)
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    // ------------ 失败响应 ----------
    public static <T> ResponseModel<T> fail(String message) {
        return ResponseModel.<T>builder()
                .status("FAIL")
                .code("500")
                .message(message)
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    public static <T> ResponseModel<T> fail(String code, String message) {
        return ResponseModel.<T>builder()
                .status("FAIL")
                .code(code)
                .message(message)
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    public static <T> ResponseModel<T> fail(String code, String message , T data) {
        return ResponseModel.<T>builder()
                .status("FAIL")
                .code(code)
                .message(message)
                .data(data)
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    // ------------ 错误响应 ----------
    public static <T> ResponseModel<T> error(String code ,String message){
        return ResponseModel.<T>builder()
                .status("ERROR")
                .message(message)
                .code(code)
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    public static <T> ResponseModel<T> error(String message , T data){
        return ResponseModel.<T>builder()
                .status("ERROR")
                .message(message)
                .code("400")
                .data(data)
                .traceId(AppContextHolder.getTraceId().get())
                .build();
    }

    public static <T> ResponseModel<T> error(String message){
        return error("400" ,message);
    }

    // -------- 响应转换----------
    public static <T>ResponseEntity<ResponseModel<T>> toResponseEntity(ResponseModel<T> responseModel){
        return ResponseEntity.ok(responseModel);
    }

    public static <T> ResponseEntity<ResponseModel<T>> toResponseEntity(ResponseModel<T> responseModel, HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus).body(responseModel);
    }

    //  ------------------快捷方法------------------

    public static <T> ResponseEntity<ResponseModel<T>> okResponse() {
        return toResponseEntity(success());
    }

    public static <T> ResponseEntity<ResponseModel<T>> okResponse(T data) {
        return toResponseEntity(success( data));
    }

    public static <T> ResponseEntity<ResponseModel<T>> okResponse(T data, HttpStatus httpStatus) {
        return toResponseEntity(success(data), httpStatus);
    }

    public static <T> ResponseEntity<ResponseModel<T>> okResponse(String message , T data) {
        return toResponseEntity(success(message ,data));
    }

    public static <T> ResponseEntity<ResponseModel<T>> errorResponse(String message) {
        return toResponseEntity(error(message));
    }

    public static <T> ResponseEntity<ResponseModel<T>> errorResponse(String code , String message) {
        return toResponseEntity(error(code ,message));
    }

    public static <T> ResponseEntity<ResponseModel<T>> errorResponse(String message , HttpStatus httpStatus , T data) {
        return toResponseEntity(error( message ,data) , httpStatus);
    }

    public static <T> ResponseEntity<ResponseModel<T>> errorResponse(String message , HttpStatus httpStatus ) {
        return toResponseEntity(error( message ) , httpStatus);
    }

    public static <T> ResponseEntity<ResponseModel<T>> failResponse(String message) {
        return toResponseEntity(fail(message));
    }

    public static <T> ResponseEntity<ResponseModel<T>> failResponse( String message , HttpStatus httpStatus) {
        return toResponseEntity(fail(message),httpStatus);
    }

}
