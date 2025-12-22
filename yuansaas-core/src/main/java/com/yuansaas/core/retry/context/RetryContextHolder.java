package com.yuansaas.core.retry.context;

/**
 * 重试上下文持有者
 *
 * @author HTB 2025/8/18 15:07
 */
public class RetryContextHolder {

    private static final ThreadLocal<Integer> attempt = ThreadLocal.withInitial(() -> 1);
    private static final ThreadLocal<Integer> maxAttempt = ThreadLocal.withInitial(() -> 1);

    public static void setAttempt(int value) {
        attempt.set(value);
    }
    public static int getAttempt() {
        return attempt.get();
    }

    public static void setMaxAttempt(int value) {
        maxAttempt.set(value);
    }
    public static int getMaxAttempt() {
        return maxAttempt.get();
    }

    public static void clear() {
        attempt.remove();
        maxAttempt.remove();
    }

}
