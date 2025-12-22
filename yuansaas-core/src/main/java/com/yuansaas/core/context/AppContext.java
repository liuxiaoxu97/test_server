package com.yuansaas.core.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 上下文数据模型
 *
 * @author HTB 2025/7/24 09:42
 */
@Accessors(chain = true)
@Data
public class AppContext implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户编号
     */
    private String userNo;

    /**
     *  用户类型
     */
    private String userType;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 链路ID
     */
    private String traceId;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 自定义属性
     */
    private Map<String , Object> customAttributes = new HashMap<>();

    private AppContext() {}

    public static AppContext create() {
        return new AppContext();
    }

    // 获取自定义属性
    public Object getAttribute(String key) {
        return customAttributes.get(key);
    }

    public <T> T getAttribute(String key, Class<T> clazz) {
        Object object = customAttributes.get(key);
        return clazz.isInstance(object) ? clazz.cast(object) : null;
    }

    public AppContext setAttribute(String key, Object value) {
        customAttributes.put(key, value);
        return this;
    }

    public AppContext removeAttribute(String key) {
        customAttributes.remove(key);
        return this;
    }

    public AppContext clearAttributes() {
        customAttributes.clear();
        return this;
    }

    public AppContext copy() {
        AppContext copy = new AppContext();
        copy.setUserId(userId);
        copy.setUserName(userName);
        copy.setUserNo(userNo);
        copy.setUserType(userType);
        copy.setClientType(clientType);
        copy.setTraceId(traceId);
        copy.setIpAddress(ipAddress);
        copy.setSessionId(sessionId);
        copy.setCustomAttributes(new HashMap<>(customAttributes));
        return copy;
    }

}
