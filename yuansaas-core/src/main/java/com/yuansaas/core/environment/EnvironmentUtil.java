package com.yuansaas.core.environment;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

/**
 * 环境工具类
 *
 * <p>提供环境检测、环境判断和环境信息获取功能</p>
 *
 * @author HTB
 * @version 1.0.0
 * @since 2025-07-22
 */
@Data
@Component
@Lazy(value = false)
public class EnvironmentUtil {

    private final Environment environment;

    private static EnvironmentUtil instance;

    /**
     * 构造函数
     */
    public EnvironmentUtil(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    private void init() {
        instance = this;
    }

    // ======================== 环境判断方法 ========================

    /**
     * 判断是否为开发环境
     */
    public static boolean isDev() {
        return getEnvironment().acceptsProfiles(Profiles.of(EnvironmentEnum.DEV.getProfile()));
    }

    /**
     * 判断是否为测试环境
     */
    public static boolean isTest() {
        return getEnvironment().acceptsProfiles(Profiles.of(EnvironmentEnum.TEST.getProfile()));
    }

    /**
     * 判断是否为生产环境
     */
    public static boolean isProd() {
        return getEnvironment().acceptsProfiles(Profiles.of(EnvironmentEnum.PROD.getProfile()));
    }

    // ======================== 环境信息获取 ========================

    /**
     * 获取当前环境枚举
     */
    public static EnvironmentEnum getCurrentEnvironment() {
        String[] activeProfiles = getEnvironment().getActiveProfiles();

        if (activeProfiles.length == 0) {
            return EnvironmentEnum.UNKNOWN;
        }

        // 优先匹配生产环境
        for (String profile : activeProfiles) {
            if (profile.equalsIgnoreCase(EnvironmentEnum.PROD.getProfile()) ) {
                return EnvironmentEnum.PROD;
            }
        }

        // 匹配其他环境
        for (String profile : activeProfiles) {
            EnvironmentEnum env = EnvironmentEnum.fromProfile(profile);
            if (env != EnvironmentEnum.UNKNOWN) {
                return env;
            }
        }

        return EnvironmentEnum.UNKNOWN;
    }

    /**
     * 获取环境描述信息
     */
    public static String getEnvironmentDescription() {
        return getCurrentEnvironment().getDesc();
    }

    /**
     * 获取所有激活的环境配置
     */
    public static String[] getActiveProfiles() {
        return getEnvironment().getActiveProfiles();
    }

    /**
     * 获取默认环境配置
     */
    public static String[] getDefaultProfiles() {
        return getEnvironment().getDefaultProfiles();
    }

    // ======================== 环境属性获取 ========================

    /**
     * 获取环境属性值
     *
     * @param key 属性键
     * @return 属性值，如果不存在返回 null
     */
    public static String getProperty(String key) {
        return getEnvironment().getProperty(key);
    }

    /**
     * 获取环境属性值（带默认值）
     *
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值，如果不存在返回默认值
     */
    public static String getProperty(String key, String defaultValue) {
        return getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * 获取环境属性值（指定类型）
     *
     * @param key 属性键
     * @param targetType 目标类型
     * @return 属性值，如果不存在返回 null
     */
    public static  <T> T getProperty(String key, Class<T> targetType) {
        return getEnvironment().getProperty(key, targetType);
    }

    /**
     * 获取环境属性值（指定类型，带默认值）
     *
     * @param key 属性键
     * @param targetType 目标类型
     * @param defaultValue 默认值
     * @return 属性值，如果不存在返回默认值
     */
    public static  <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return getEnvironment().getProperty(key, targetType, defaultValue);
    }

    // ======================== 环境特征检测 ========================

    /**
     * 是否启用调试模式
     */
    public static boolean isDebugEnabled() {
        return getEnvironment().acceptsProfiles(Profiles.of("debug")) ||
                Boolean.parseBoolean(getProperty("debug.enabled", "false"));
    }

    /**
     * 是否启用性能监控
     */
    public static boolean isPerformanceMonitoringEnabled() {
        return getEnvironment().acceptsProfiles(Profiles.of("perf")) ||
                Boolean.parseBoolean(getProperty("performance.monitoring.enabled", "false"));
    }

    /**
     * 是否启用缓存
     */
    public static boolean isCacheEnabled() {
        return !isDev() || Boolean.parseBoolean(getProperty("cache.enabled", "true"));
    }

    /**
     * 是否启用安全控制
     */
    public static boolean isSecurityEnabled() {
        return !isDev() || Boolean.parseBoolean(getProperty("security.enabled", "true"));
    }

    /**
     * 获取 Environment 实例
     */
    private static Environment getEnvironment() {
        if (instance == null) {
            throw new IllegalStateException("EnvironmentUtil 尚未初始化，请确保它已被 Spring 管理");
        }
        return instance.environment;
    }
}