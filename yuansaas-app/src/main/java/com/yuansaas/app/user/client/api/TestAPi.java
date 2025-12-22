package com.yuansaas.app.user.client.api;

import com.yuansaas.integration.common.enums.CallType;
import com.yuansaas.integration.common.enums.ServiceType;
import com.yuansaas.integration.common.log.annotations.ThirdPartyLog;
import com.yuansaas.integration.wx.mp.service.WxMpAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * 测试API
 *
 * @author HTB 2025/7/21 14:26
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestAPi {


    private final WebClient webClient;

    private final WxMpAuthService wxMpAuthService;



    /**
     * 测试API
     * @return 测试结果
     */
    @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @ThirdPartyLog(serviceName = ServiceType.WX_MP, action = "test" , callType = CallType.HTTP)
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<String> test() {
        webClient.post()
               .uri("https://a-beta.yljk.cn/yft/api/dict")
                .bodyValue(Map.of("openId","测试openId", "refreshToken","测试refreshToken"))
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.e")
               .retrieve()
               .bodyToMono(String.class)
               .block();
        return ResponseEntity.ok("OK");
    }

    /**
     * 测试API
     * @return 测试结果
     */
//    @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @ThirdPartyLog(serviceName = ServiceType.WX_MP, callType = CallType.HTTP)
    @RequestMapping(value = "/get/access_token", method = RequestMethod.POST)
    public ResponseEntity<String> getAccessToken() {
        return ResponseEntity.ok(wxMpAuthService.getAccessToken());
    }

}
