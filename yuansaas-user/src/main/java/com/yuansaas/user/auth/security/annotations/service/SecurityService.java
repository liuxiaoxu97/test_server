package com.yuansaas.user.auth.security.annotations.service;

import cn.hutool.core.util.ObjectUtil;
import com.yuansaas.user.auth.model.CustomUserDetails;
import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import com.yuansaas.user.common.enums.UserType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证处理
 *
 * @author HTB 2025/8/12 16:16
 */
@Service
public class SecurityService {

    /**
     * 组合认证检查
     */
    public boolean checkCombinedAuth(Authentication authentication, SecurityAuth annotation) {
        // 获取注解属性
        UserType[] userTypes = annotation.userTypes();
        String[] roles = annotation.roles();
        String[] permissions = annotation.permissions();

        // 检查接口是否需要token
        if (annotation.authenticated() ) {
            if (authentication instanceof UsernamePasswordAuthenticationToken && ObjectUtil.isNull(authentication.getPrincipal())) {
                return false;
            }
            if (authentication instanceof AnonymousAuthenticationToken) {
                return false;
            }
        }

        // 1. 检查认证状态
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 2. 检查用户类型
        if (userTypes!= null && userTypes.length > 0 && !checkUserTypes(authentication, userTypes)) {
            return false;
        }

        // 3. 检查角色
        if (roles!= null && roles.length > 0 && !checkRoles(authentication, roles)) {
            return false;
        }

        // 4. 检查权限
        if (permissions!= null && permissions.length > 0 && !checkPermissions(authentication, permissions)) {
            return false;
        }

        return true;
    }

    private boolean checkUserTypes(Authentication authentication, UserType[] requiredTypes) {
        UserType currentUserType = extractUserType(authentication);
        return Arrays.stream(requiredTypes).anyMatch(t -> t == currentUserType);
    }

    private boolean checkRoles(Authentication authentication, String[] requiredRoles) {
        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Arrays.stream(requiredRoles).anyMatch(userRoles::contains);
    }

    private boolean checkPermissions(Authentication authentication, String[] requiredPermissions) {
        // todo 实现具体权限检查逻辑
        return true;
    }

    private UserType extractUserType(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserType();
        }
        return UserType.GUEST_USER;
    }
}
