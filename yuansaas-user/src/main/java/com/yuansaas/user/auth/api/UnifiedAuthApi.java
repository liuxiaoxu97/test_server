package com.yuansaas.user.auth.api;

import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.param.RefreshTokenParam;
import com.yuansaas.user.auth.param.UnifiedLoginParam;
import com.yuansaas.user.auth.service.UnifiedAuthService;
import com.yuansaas.user.auth.vo.AuthVo;
import com.yuansaas.user.auth.vo.TokenRefreshVo;
import com.yuansaas.user.common.enums.UserType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 *
 * @author HTB 2025/8/11 15:15
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UnifiedAuthApi {

    private final UnifiedAuthService authService;

    /**
     * 登录接口
     * @param request 登录参数
     * @param httpRequest http请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseModel<AuthVo>> login(
            @RequestBody @Valid UnifiedLoginParam request,
            HttpServletRequest httpRequest) {

        AuthVo response = authService.login(request, httpRequest);
        return ResponseBuilder.okResponse(response);
    }

    /**
     * 刷新令牌接口
     */
    @PostMapping("/refresh")
    public ResponseEntity<ResponseModel<TokenRefreshVo>> refreshToken(
            @RequestBody @Valid RefreshTokenParam refreshTokenParam) {

        TokenRefreshVo response = authService.refreshToken(refreshTokenParam.getRefreshToken());
        return ResponseBuilder.okResponse(response);
    }

    /**
     * 登出接口
     * @param token token
     * @return 登出结果
     */
    @PostMapping("/logout")
    public ResponseEntity<ResponseModel<Void>> logout(
            @RequestHeader("Authorization") String token) {

        authService.logout(token);
        return ResponseBuilder.okResponse();
    }

    /**
     * 锁定用户接口
     * @param userId 用户id
     * @param userType 用户类型
     * @return 锁定结果
     */
    @PostMapping("/lock/{userId}/{userType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<Void>> lockUser(
            @PathVariable Long userId,
            @PathVariable UserType userType) {

        authService.lockUser(userId, userType);
        return ResponseBuilder.okResponse();
    }

    /**
     * 解锁用户接口
     * @param userId 用户id
     * @param userType 用户类型
     * @return 解锁结果
     */
    @PostMapping("/unlock/{userId}/{userType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel<Void>> unlockUser(
            @PathVariable Long userId,
            @PathVariable UserType userType) {

        authService.unlockUser(userId, userType);
        return ResponseBuilder.okResponse();
    }

}
