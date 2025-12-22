package com.yuansaas.integration.common.log.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.retry.context.RetryContextHolder;
import com.yuansaas.integration.common.enums.CallType;
import com.yuansaas.integration.common.log.annotations.ThirdPartyLog;
import com.yuansaas.integration.common.log.context.ThirdPartyLogContext;
import com.yuansaas.integration.common.log.entity.ThirdPartyCallLog;
import com.yuansaas.integration.common.log.repository.ThirdPartyCallLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 日志记录AOP
 *
 * @author HTB 2025/8/13 17:22
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ThirdPartyLogAspect {

    private final ThirdPartyCallLogRepository logRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.yuansaas.integration.common.log.annotations.ThirdPartyLog)")
    public Object logThirdPartyCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ThirdPartyLog annotation = signature.getMethod().getAnnotation(ThirdPartyLog.class);

        String serviceName = annotation.serviceName().name();
        String action = annotation.action().isEmpty() ? signature.getMethod().getName() : annotation.action();
        CallType callType = annotation.callType();

        long start = System.currentTimeMillis();
        String requestJson = serializeSafe(joinPoint.getArgs());

        try {
            // 尝试从 ThreadLocal 先取（阻塞场景）

            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;

            ThirdPartyCallLog logEntity = new ThirdPartyCallLog()
                    .setServiceName(serviceName)
                    .setTraceId(AppContextUtil.requireTraceId())
                    .setAction(action)
                    .setCallType(callType.name())
                    .setRequestUri(ThirdPartyLogContext.getRequestUri())
                    .setRequestData(requestJson)
                    .setResponseStatus( Optional.ofNullable(ThirdPartyLogContext.getResponseStatus()).orElse("200"))
                    .setResponseData(serializeSafe(result))
                    .setDurationMs(duration)
                    .setResult("SUCCESS")
                    .setAttempt(RetryContextHolder.getAttempt());
            logEntity.setCreateAt(LocalDateTime.now());
            logEntity.setCreateBy(AppContextUtil.getUserInfo());

            saveLogAsync(logEntity);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;

            ThirdPartyCallLog logEntity = new ThirdPartyCallLog()
                    .setServiceName(serviceName)
                    .setTraceId(AppContextUtil.requireTraceId())
                    .setAction(action)
                    .setCallType(callType.name())
                    .setRequestUri(ThirdPartyLogContext.getRequestUri())
                    .setRequestData(requestJson)
                    .setResponseStatus(Optional.ofNullable(ThirdPartyLogContext.getResponseStatus()).orElse("N/A"))
                    .setErrorMsg(e.getMessage())
                    .setDurationMs(duration)
                    .setResult("ERROR")
                    .setAttempt(RetryContextHolder.getAttempt());
            logEntity.setCreateAt(LocalDateTime.now());
            logEntity.setCreateBy(AppContextUtil.getUserInfo());

            saveLogAsync(logEntity);
            throw e;
        } finally {
            ThirdPartyLogContext.clear();
        }
    }

    private String serializeSafe(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "[unserializable]";
        }
    }

    @Async
    public void saveLogAsync(ThirdPartyCallLog logEntity) {
        try {
            logRepository.save(logEntity);
        } catch (Exception e) {
            log.error("保存第三方调用日志失败: {}", e.getMessage(), e);
        }
    }
}
