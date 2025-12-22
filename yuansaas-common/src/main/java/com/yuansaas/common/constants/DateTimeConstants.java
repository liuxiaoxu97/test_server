package com.yuansaas.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 日期时间常量
 *
 * @author HTB 2025/7/25 11:10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeConstants {

    // 日期格式
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";

    // 时间格式
    public static final String TIME_FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String TIME_FORMAT_HHMMSS = "HHmmss";

    // 日期时间格式
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    // 时间单位（毫秒）
    public static final long MILLIS_PER_SECOND = 1000;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
    public static final long MILLIS_PER_WEEK = 7 * MILLIS_PER_DAY;

    // 时区
    public static final String TIMEZONE_UTC = "UTC";
    public static final String TIMEZONE_ASIA_SHANGHAI = "Asia/Shanghai";
    public static final String TIMEZONE_AMERICA_NEW_YORK = "America/New_York";
}
