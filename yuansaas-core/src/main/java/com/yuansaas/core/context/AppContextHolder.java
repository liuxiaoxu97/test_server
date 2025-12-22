package com.yuansaas.core.context;

import java.util.Optional;

/**
 * 上下文信息持有者
 *
 * @author HTB 2025/7/24 09:54
 */
public final class AppContextHolder {

    private static final ThreadLocal<AppContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private static  final InheritableThreadLocal<AppContext> INHERITABLE_CONTEXT_HOLDER = new InheritableThreadLocal<>();

    private AppContextHolder() {
        // 私有构造器，防止创建实例
    }

    /**
     * 设置当前线程上下文信息
     */
    public static void setContext(AppContext context) {
        CONTEXT_HOLDER.set(context);
    }

    /**
     * 设置可继承的上下文信息(用于异步任务)
     */
    public static void setInheritableContext(AppContext  context){
        INHERITABLE_CONTEXT_HOLDER.set(context);
    }

    /**
     * 获取当前上下文信息
     */
    public static Optional<AppContext> getContext() {
        AppContext appContext = CONTEXT_HOLDER.get();
        if(appContext == null){
            appContext = INHERITABLE_CONTEXT_HOLDER.get();
        }
        return Optional.ofNullable(appContext);
    }

    /**
     * 获取当前上下文信息，如果为空则创建一个新的上下文信息并设置到当前线程
     */
    public static AppContext getOrCreateContext() {
        return getContext().orElseGet(() -> {
            AppContext appContext = AppContext.create()
                    .setTraceId(TraceIdContext.getTraceId());
            setContext(appContext);
            return appContext;
        });
    }

    /**
     * 清除当前线程上下文信息
     */
    public static void clearContext() {
        CONTEXT_HOLDER.remove();
        INHERITABLE_CONTEXT_HOLDER.remove();
    }

    // 获取上下文信息
    public static Optional<Long> getUserId() {
        return getContext().map(AppContext::getUserId);
    }

    public static Optional<String> getUserName() {
        return getContext().map(AppContext::getUserName);
    }

    public static Optional<String> getUserNo() {
        return getContext().map(AppContext::getUserNo);
    }

    public static Optional<String> getUserType() {
        return getContext().map(AppContext::getUserType);
    }

    public static Optional<String> getClientType() {
        return getContext().map(AppContext::getClientType);
    }

    public static Optional<String> getTraceId() {
        return getContext().map(AppContext::getTraceId);
    }

    public static Optional<String> getIpAddress() {
        return getContext().map(AppContext::getIpAddress);
    }

    public static Optional<String> getSessionId() {
        return getContext().map(AppContext::getSessionId);
    }

    public static Optional<Object> getAttribute(String key) {
        return getContext().flatMap(context -> Optional.ofNullable(context.getAttribute(key)));
    }

    public static <T> Optional<T> getAttribute(String key, Class<T> clazz) {
        return getContext().flatMap(context -> Optional.ofNullable(context.getAttribute(key, clazz)));
    }

}
