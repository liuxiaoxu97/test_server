package com.yuansaas.user.auth.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新token的参数
 *
 * @author HTB 2025/8/12 15:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenParam {

    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;

}
