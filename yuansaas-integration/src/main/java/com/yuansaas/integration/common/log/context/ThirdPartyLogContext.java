package com.yuansaas.integration.common.log.context;

/**
 * 上下文
 *
 * @author HTB 2025/8/13 17:06
 */
public class ThirdPartyLogContext {

    private static final ThreadLocal<String> requestUri = new ThreadLocal<>();
    private static final ThreadLocal<String> responseStatus = new ThreadLocal<>();

    public static void setRequestUri(String uri) {
        requestUri.set(uri);
    }

    public static String getRequestUri() {
        return requestUri.get();
    }

    public static void setResponseStatus(String status) {
        responseStatus.set(status);
    }

    public static String getResponseStatus() {
        return responseStatus.get();
    }

    public static void clear() {
        requestUri.remove();
        responseStatus.remove();
    }
}
