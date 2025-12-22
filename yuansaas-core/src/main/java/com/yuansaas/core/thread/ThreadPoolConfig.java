package com.yuansaas.core.thread;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executors;

/**
 * 线程池配置
 *
 * @author HTB 2025/7/23 10:14
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        // 创建一个基于虚拟线程的线程池
        return new ConcurrentTaskExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        // 创建一个基于虚拟线程的 Tomcat 协议处理器定制器 让所有的tomcat 请求都基于虚拟线程处理
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

}
