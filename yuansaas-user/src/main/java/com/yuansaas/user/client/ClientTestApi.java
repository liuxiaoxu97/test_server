package com.yuansaas.user.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端API 测试
 *
 * @author HTB 2025/7/31 12:07
 */
@RestController
public class ClientTestApi {

    /**
     * 测试API接口
     * @return 测试结果
     */
    @GetMapping("/client/test")
    public String test() {
        return "Hello, Client User!";
    }

}
