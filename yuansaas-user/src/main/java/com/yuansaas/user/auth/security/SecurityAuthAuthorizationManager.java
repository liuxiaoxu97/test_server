package com.yuansaas.user.auth.security;

import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import com.yuansaas.user.auth.security.annotations.service.SecurityService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 安全认证
 *
 * @author HTB 2025/8/12 16:58
 */
public class SecurityAuthAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    private final SecurityService securityService;

    public SecurityAuthAuthorizationManager(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation invocation) {
        Authentication auth = authentication.get();

        // 获取注解（方法优先，类次之）
        SecurityAuth annotation = findAnnotation(invocation);

        if (annotation == null) {
            return new AuthorizationDecision(true); // 没有注解默认放行
        }

        boolean result = securityService.checkCombinedAuth(auth, annotation);
        return new AuthorizationDecision(result);
    }

    private SecurityAuth findAnnotation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        return Optional.ofNullable(method.getAnnotation(SecurityAuth.class))
                .orElse(invocation.getThis().getClass().getAnnotation(SecurityAuth.class));
    }
}
