package com.yuansaas.user.auth.security;

import com.yuansaas.user.auth.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * 令牌解析
 *
 * @author HTB 2025/8/11 17:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenParser {

    private final JwtProperties jwtProperties;

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("令牌已过期: {}", ex.getMessage());
            throw ex;
        } catch (MalformedJwtException ex) {
            log.warn("无效的令牌格式: {}", ex.getMessage());
            return false;
        } catch (SignatureException ex) {
            log.warn("令牌签名无效: {}", ex.getMessage());
            return false;
        } catch (Exception ex) {
            log.warn("令牌验证失败: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * 获取 JWT 过期时间
     * @param token JWT 访问令牌
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 获取 刷新令牌过期时间
     * @return 过期时间
     */
    public long extractRefreshExpiration() {
        return jwtProperties.getRefreshExpiration();
    }

    /**
     * 获取 JWT 用户名
     * @param token JWT 访问令牌
     * @return 用户名
     */
    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 获取 JWT token 类型
     * @param token JWT 访问令牌
     * @return token 类型
     */
    public String extractTokenType(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("tokenType", String.class);
    }

    /**
     * 获取 JWT 信息
     * @param token JWT 访问令牌
     * @param claimsResolver 解析 claims 的函数
     * @return claims
     * @param <T> claims 的类型
     */
    private <T> T extractClaim(String token, Function<Claims ,  T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 获取 JWT 所有信息
     * @param token  JWT 访问令牌
     * @return claims
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws( token)
                .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
