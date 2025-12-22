package com.yuansaas.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 正则表达式常量
 *
 * @author HTB 2025/7/25 11:12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexConstants {

    public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String PHONE_CN = "^1[3-9]\\d{9}$"; // 中国大陆手机号
    public static final String ID_CARD_CN = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$"; // 中国大陆身份证
    public static final String URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    public static final String IPV4 = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    public static final String NUMBER = "^[-+]?\\d+(\\.\\d+)?$";
    public static final String INTEGER = "^[-+]?\\d+$";
    public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";
    public static final String NON_NEGATIVE_INTEGER = "^\\d+$";
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";
    public static final String DATE_YYYY_MM_DD = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
    public static final String TIME_HH_MM_SS = "^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$";
    public static final String USERNAME = "^[a-zA-Z0-9_]{3,20}$";
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$";

}
