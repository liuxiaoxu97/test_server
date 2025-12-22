package com.yuansaas.core.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 上下文工具类
 *
 * @author HTB 2025/7/24 10:12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppContextUtil {

    /**
     * 运行在指定上下文中  返回值
     * @param appContext 上下文
     * @param runnable 运行函数
     * @return 运行结果
     */
    public static <T, R> R runInContext(AppContext appContext, Function<AppContext, R> runnable) {
        AppContext previousContext = AppContextHolder.getContext().orElse(null);
        try {
            AppContextHolder.setContext(appContext);
            return runnable.apply(appContext);
        } finally {
           restoreContext(previousContext);
        }
    }

    /**
     * 运行在指定上下文中 不返回值
     * @param appContext 上下文
     * @param consumer 运行函数
     */
    public static void runInContext(AppContext appContext, Consumer<AppContext> consumer) {
        runInContext(appContext, ctx -> {
            consumer.accept(ctx);
            return null;
        });
    }

    /**
     * 运行在指定上下文中 返回值
     * @param appContext 上下文
     * @param supplier 运行函数
     * @return 运行结果
     */
    public static <R> R runInContext(AppContext appContext , Supplier<R> supplier){
        return runInContext(appContext, ctx -> {
            return supplier.get();
        });
    }

    /**
     * 运行在指定上下文中 不返回值
     * @param appContext 上下文
     * @param runnable 运行函数
     */
    public static void runInContext(AppContext appContext, Runnable runnable){
        runInContext(appContext, ctx -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 获取当前用户ID
     * @return 当前用户ID
     */
    public static Long requireUserId() {
        return AppContextHolder.getUserId().orElseThrow(() -> new SecurityException("User ID not available in context"));
    }

    /**
     * 获取当前TraceId
     * @return 当前TraceId
     */
    public static String requireTraceId(){
        return AppContextHolder.getTraceId().orElse("unknown");
    }

    /**
     * 获取当前ClientType
     * @return 当前ClientType
     */
    public static String requireClientType(){
        return AppContextHolder.getClientType().orElse("");
    }

    /**
     * 安全获取自定义属性（必须存在）
     */
    public static <T> T requireAttribute(String key, Class<T> type) {
        return AppContextHolder.getAttribute(key, type)
                .orElseThrow(() -> new IllegalArgumentException("Attribute not found: " + key));
    }

    /**
     * 获取用户ID和用户名的拼接，用:分隔
     * @return 用户ID和用户名的拼接
     */
    public static String getUserInfo() {
        return AppContextHolder.getUserId().map(id -> id + ":" + AppContextHolder.getUserName().orElse("")).orElse("");
    }

    /**
     * 创建上下文副本
     * @return 上下文副本
     */
    public static AppContext createContextCopy() {
        return AppContextHolder.getContext().map(AppContext::copy).orElse(null);
    }

    /**
     * 恢复上下文
     * @param previousContext  上一个下文
     */
    public static void restoreContext(AppContext previousContext) {
        if (previousContext == null) {
            AppContextHolder.clearContext();
        } else {
            AppContextHolder.setContext(previousContext);
        }
    }
}
