package com.yuansaas.user.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新token 响应
 *
 * @author HTB 2025/8/12 15:18
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshVo {

    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌剩余秒数
     */
    private long expiresIn;
}
