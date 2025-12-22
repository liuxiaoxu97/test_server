package com.yuansaas.core.environment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 环境枚举类
 *
 * @author HTB 2025/7/22 18:50
 */
@Getter
@AllArgsConstructor
public enum EnvironmentEnum {

    DEV("dev", "开发环境"),
    TEST("test", "测试环境"),
    PROD("prod", "生产环境"),
    UNKNOWN("unknown", "未知环境")
    ;
    /**
     * 别名
     */
    private final String profile;
    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据环境代码获取枚举
     */
    public static EnvironmentEnum fromProfile(String profile) {
        for (EnvironmentEnum env : values()) {
            if (env.profile.equalsIgnoreCase(profile)) {
                return env;
            }
        }
        return UNKNOWN;
    }
}
