package com.yuansaas.integration.common.log.annotations;

import com.yuansaas.integration.common.enums.CallType;
import com.yuansaas.integration.common.enums.ServiceType;

import java.lang.annotation.*;

/**
 * 日志记录
 *
 * @author HTB 2025/8/13 17:08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThirdPartyLog {

    /**
     * 服务名称
     * @return 服务名称
     */
    ServiceType serviceName();

    /**
     * 接口或方法名称
     * @return 接口或方法名称
     */
    String action() default "";

    /**
     * 调用方式: URI / SDK
     */
    CallType callType() ;


}
