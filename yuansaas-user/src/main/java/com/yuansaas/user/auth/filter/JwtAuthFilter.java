package com.yuansaas.user.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuansaas.core.exception.AppException;
import com.yuansaas.core.exception.ex.AuthErrorCode;
import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.model.CustomUserDetails;
import com.yuansaas.user.auth.security.JwtManager;
import com.yuansaas.user.auth.security.JwtTokenParser;
import com.yuansaas.user.common.service.UserStatusCache;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 *
 * @author HTB 2025/8/11 14:23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    private final JwtTokenParser jwtTokenParser;
    private final ObjectMapper globalMapper;
    private final UserStatusCache userStatusCache;

    /**
     * 拦截器
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤链
     * @throws ServletException 抛出ServletException
     * @throws IOException 抛出IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
            try {
                String token = extractToken(request);
                if (token != null && jwtManager.validateToken(token)) {
                    // 检查令牌类型
                    String tokenType = jwtTokenParser.extractTokenType(token);

                    // 如果是refresh token，拒绝访问非刷新接口
                    if ("refresh".equals(tokenType) && !isRefreshEndpoint(request)) {
                        throw AuthErrorCode.INSUFFICIENT_PERMISSIONS.buildException();
                    }
                    // 解析 JWT 访问令牌 并设置认证信息
                    CustomUserDetails userDetails = jwtManager.parseToken(token);

                    // 验证用户状态
                    if (!userStatusCache.isUserActive(userDetails.getUserId(), userDetails.getUserType())) {
                        throw AuthErrorCode.ACCOUNT_DISABLED.buildException();
                    }

                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                handleAuthenticationException(response, e);
                return;
            }

        filterChain.doFilter(request, response);
    }

    /**
     * 判断是否是刷新令牌请求
     * @param request 请求
     * @return 是否是刷新令牌请求
     */
    private boolean isRefreshEndpoint(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod()) &&
                request.getRequestURI().endsWith("/auth/refresh");
    }

    /**
     * 判断是否是登录请求
     * @param request 请求
     * @return  是否是登录请求
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        log.error("无效的认证令牌 token: {}", bearerToken);
        return null;
    }

    /**
     * 认证失败处理
     * @param response 响应
     * @param e 认证异常
     * @throws IOException 抛出IOException
     */
    private void handleAuthenticationException(HttpServletResponse response, Exception e)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorMessage;
        ResponseModel<String> responseModel ;
        if (e instanceof ExpiredJwtException) {
            errorMessage = "令牌已过期";
            responseModel = ResponseBuilder.error(AuthErrorCode.TOKEN_EXPIRED.getCode(), errorMessage);
        } else if (e instanceof DisabledException || e instanceof AppException) {
            errorMessage = e.getMessage();
            responseModel = ResponseBuilder.error(AuthErrorCode.AUTHENTICATION_FAILED.getCode(), errorMessage);
        } else {
            errorMessage = "无效的认证令牌";
            responseModel = ResponseBuilder.error(AuthErrorCode.INVALID_TOKEN.getCode(), errorMessage);
        }
        globalMapper.writeValue(response.getWriter(), responseModel);
    }
}
