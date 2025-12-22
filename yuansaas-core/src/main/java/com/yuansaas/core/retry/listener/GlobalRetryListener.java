package com.yuansaas.core.retry.listener;

import com.yuansaas.core.retry.context.RetryContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

/**
 * 全局重试监听
 *
 * @author HTB 2025/8/13 17:45
 */
@Slf4j
@Component
public class GlobalRetryListener implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        RetryContextHolder.setAttempt(1);
        RetryContextHolder.setMaxAttempt(context.getRetryCount() + 1); // +1 表示第一次调用
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        // 结束后清理
        RetryContextHolder.clear();
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        int attemptNumber = context.getRetryCount() + 1;
        RetryContextHolder.setAttempt(attemptNumber);
        log.warn("[Retry] 第 {} 次尝试失败: {}", attemptNumber, throwable.getMessage());
    }
}
