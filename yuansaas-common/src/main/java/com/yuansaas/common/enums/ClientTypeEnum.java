package com.yuansaas.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 客户端类型
 *
 * @author HTB 2025/7/25 11:16
 */
@Getter
public enum ClientTypeEnum implements IBaseEnum<ClientTypeEnum> {

    WEB("web", "Web浏览器"),
    H5("h5" , "H5浏览器"),
    MOBILE_WEB("mobile_web", "移动端浏览器"),
    ANDROID("android", "Android应用"),
    IOS("ios", "iOS应用"),
    WINDOWS("windows", "Windows应用"),
    MAC("mac", "Mac应用"),
    LINUX("linux", "Linux应用"),
    WEIXIN_MINI_PROGRAM("weixin_mini", "微信小程序"),
    ALIPAY_MINI_PROGRAM("alipay_mini", "支付宝小程序"),
    DOUYIN_MINI_PROGRAM("douyin_mini", "抖音小程序"),
    HARMONYOS("harmonyos", "鸿蒙应用"),
    OTHER("other", "其他客户端"),
    UNKNOWN("unknown", "未知客户端");

    private final String value;
    private final String description;

    private static final Map<String, ClientTypeEnum> VALUE_MAP = Arrays.stream(values())
            .collect( Collectors.toMap(ClientTypeEnum::getValue, Function.identity()));

    ClientTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 根据值获取枚举实例
     */
    public static ClientTypeEnum fromValue(String value) {
        return VALUE_MAP.getOrDefault(value, UNKNOWN);
    }

    /**
     * 根据值获取枚举实例（忽略大小写）
     */
    public static ClientTypeEnum fromValueIgnoreCase(String value) {
        if (value == null) return UNKNOWN;

        String lowerValue = value.toLowerCase();
        return VALUE_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(lowerValue))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(UNKNOWN);
    }

    /**
     * 根据User-Agent判断客户端类型
     */
    public static ClientTypeEnum fromUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return UNKNOWN;
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("micromessenger")) {
            // 微信环境
            if (ua.contains("miniprogram")) {
                return WEIXIN_MINI_PROGRAM;
            }
            return MOBILE_WEB; // 微信内置浏览器
        }

        if (ua.contains("alipayclient")) {
            // 支付宝环境
            if (ua.contains("miniprogram")) {
                return ALIPAY_MINI_PROGRAM;
            }
            return MOBILE_WEB; // 支付宝内置浏览器
        }

        if (ua.contains("toutiaomicroapp")) {
            return DOUYIN_MINI_PROGRAM; // 抖音小程序
        }

        if (ua.contains("android")) {
            return ANDROID;
        }

        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("mac os")) {
            return IOS;
        }

        if (ua.contains("windows")) {
            return WINDOWS;
        }

        if (ua.contains("macintosh")) {
            return MAC;
        }

        if (ua.contains("linux")) {
            return LINUX;
        }

        if (ua.contains("harmonyos")) {
            return HARMONYOS;
        }

        // 移动端浏览器检测
        if (ua.contains("mobile") || ua.contains("mobi") || ua.contains("phone")) {
            return MOBILE_WEB;
        }
        // 默认认为是Web浏览器
        return WEB;
    }

    /**
     * 检查是否是移动端
     */
    public boolean isMobile() {
        return this == ANDROID ||
                this == IOS ||
                this == MOBILE_WEB ||
                this == WEIXIN_MINI_PROGRAM ||
                this == ALIPAY_MINI_PROGRAM ||
                this == DOUYIN_MINI_PROGRAM;
    }

    /**
     * 检查是否是桌面端
     */
    public boolean isDesktop() {
        return this == WINDOWS ||
                this == MAC ||
                this == LINUX;
    }

    /**
     * 检查是否是小程序
     */
    public boolean isMiniProgram() {
        return this == WEIXIN_MINI_PROGRAM ||
                this == ALIPAY_MINI_PROGRAM ||
                this == DOUYIN_MINI_PROGRAM;
    }

    /**
     * 检查是否是Web浏览器
     */
    public boolean isWebBrowser() {
        return this == WEB || this == MOBILE_WEB;
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return this.name();
    }

    /**
     * 检查是否匹配指定值
     */
    @Override
    public boolean matches(String value) {
        return this.value.equalsIgnoreCase(value);
    }

    /**
     * 检查是否匹配指定枚举
     */
    @Override
    public boolean matches(ClientTypeEnum other) {
        return this == other;
    }

    /**
     * 检查是否不匹配指定值
     */
    @Override
    public boolean mismatches(String value) {
        return !matches(value);
    }

    /**
     * 检查是否不匹配指定枚举
     */
    @Override
    public boolean mismatches(ClientTypeEnum other) {
        return this != other;
    }

    /**
     * 根据名称获取枚举实例
     */
    public static ClientTypeEnum fromName(String name) {
        if (name == null) return UNKNOWN;
        try {
            return ClientTypeEnum.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    /**
     * 获取所有枚举值映射
     */
    public static Map<String, String> getValueDescriptionMap() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(
                        ClientTypeEnum::getValue,
                        ClientTypeEnum::getDescription
                ));
    }
}
