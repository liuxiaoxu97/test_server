package com.yuansaas.core.context;

import java.util.UUID;

/**
 * TraceId 上下文工具类
 *
 * @author HTB 2025/7/22 14:49
 */
public final class TraceIdContext {

    private static final ThreadLocal<String> traceIdHolder = new ThreadLocal<>();

    public static String getTraceId() {
        return traceIdHolder.get();
    }

    public static void setTraceId(String traceId) {
        TraceIdContext.traceIdHolder.set(traceId);
    }

    public static void clear() {
        traceIdHolder.remove();
    }

    public static void start() {
        traceIdHolder.set(UUID.randomUUID().toString());
    }

}
