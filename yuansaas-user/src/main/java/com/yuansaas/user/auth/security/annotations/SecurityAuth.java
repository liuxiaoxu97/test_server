package com.yuansaas.user.auth.security.annotations;

import com.yuansaas.user.common.enums.UserType;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证
 *
 * @author HTB 2025/8/12 16:14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("true")
public @interface SecurityAuth {

    /**
     * 允许的用户类型
     */
    UserType[] userTypes() default {};

    /**
     * 允许的角色
     */
    String[] roles() default {};

    /**
     * 需要的权限
     */
    String[] permissions() default {};

    /**
     * 是否要求必须认证（默认true）
     */
    boolean authenticated() default true;

    /**
     * 自定义SpEL表达式
     */
    String expression() default "";

}
