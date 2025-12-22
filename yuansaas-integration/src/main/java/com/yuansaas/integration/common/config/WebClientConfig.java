package com.yuansaas.integration.common.config;

import com.yuansaas.integration.common.log.context.ThirdPartyLogContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * web 客户端调用配置
 *
 * @author HTB 2025/8/13 17:02
 */
@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        HttpClient httpClient = HttpClient.create();
        return builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("[HTTP REQUEST] {} {}", clientRequest.method(), clientRequest.url());
            // 写入 ThreadLocal（阻塞场景）
            ThirdPartyLogContext.setRequestUri(clientRequest.url().toString());
            // 因为使用 的虚拟线程。所以会使用阻塞获取。所以不用设置context
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("[HTTP RESPONSE] Status: {}", clientResponse.statusCode());
            // 写入 ThreadLocal（阻塞场景）
            ThirdPartyLogContext.setResponseStatus(String.valueOf(clientResponse.statusCode().value()));
            // 因为使用 的虚拟线程。所以会使用阻塞获取。所以不用设置context
            return Mono.just(clientResponse);
        });
    }

}
