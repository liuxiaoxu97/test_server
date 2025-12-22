package com.yuansaas.core.context.constans;

/**
 * 通用的请求头常量类
 *
 * @author HTB 2025/7/24 17:20
 */
public interface GenericHeaderCons {

    String TRACE_ID = "X-Trace-Id";

    String CLIENT_TYPE = "X-Client-Type";

    String X_REQUEST_ID = "X-Request-Id";

    String X_FORWARDED_FOR = "x-forwarded-for";

    String X_REAL_IP  = "X-Real-IP";

    String USER_AGENT = "User-Agent";

}
