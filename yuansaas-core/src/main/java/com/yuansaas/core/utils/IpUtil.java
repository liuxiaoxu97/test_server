package com.yuansaas.core.utils;

import com.yuansaas.core.context.constans.GenericHeaderCons;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Ip工具类
 *
 * @author HTB 2025/7/24 17:17
 */
public class IpUtil {


    /**
     * 获取客户端 IP 地址
     * 1. 检查 X-Real-IP 头部
     * 2. 如果 X-Real-IP 头部不存在，检查 X-Forwarded-For 头部
     * 3. 如果以上都无效，使用远程地址
     * 4. 最终验证并返回
     * @param request 请求对象
     * @return 客户端 IP 地址
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        // 1. 优先从可信的代理服务器头获取IP
        String ip = request.getHeader(GenericHeaderCons.X_REAL_IP);

        // 2. 如果X-Real-IP不存在，检查X-Forwarded-For
        if (isInvalidIp(ip)) {
            ip = getFirstValidIpFromXFF(request.getHeader(GenericHeaderCons.X_FORWARDED_FOR));
        }

        // 3. 如果以上都无效，使用远程地址
        if (isInvalidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 4. 最终验证并返回
        return validateIp(ip) ? ip : "0.0.0.0";
    }

    /**
     * 判断IP是否有效
     * @param ip IP地址
     * @return true：有效；false：无效
     */
    private static boolean isInvalidIp(String ip) {
        return ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip);
    }

    /**
     * 从X-Forwarded-For头部获取第一个有效IP地址
     * @param xffHeader X-Forwarded-For头部
     * @return 第一个有效IP地址
     */
    private static String getFirstValidIpFromXFF(String xffHeader) {
        if (isInvalidIp(xffHeader)) {
            return null;
        }

        String[] ips = xffHeader.split(",");
        for (String ip : ips) {
            ip = ip.trim();
            if (validateIp(ip)) {
                return ip;
            }
        }
        return null;
    }

    /**
     * 验证IP地址是否有效
     * 1. IPv4格式：xxx.xxx.xxx.xxx
     * 2. IPv6格式：xxxx:xxxx:xxxx:xxxx:xxxx
     * @param ip IP地址
     * @return true：有效；false：无效
     */
    private static boolean validateIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        // 验证IPv4格式
        if (ip.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
            return true;
        }

        // 验证IPv6格式
        return ip.matches("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    }

}
