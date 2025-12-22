package com.yuansaas.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 全局常量类
 *
 * @author HTB 2025/7/25 10:58
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstants {

    public static final String PWD = "DC483E80A7A0BD9EF71D8CF973673924";

    public static final String UNKNOWN = "unknown";
    public static final String DEFAULT = "default";
    public static final String N_A = "N/A";
    public static final String NULL_STRING = "null";
    public static final String ALL = "all";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String OK = "ok";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String PENDING = "pending";
    public static final String COMPLETED = "completed";
    public static final String CANCELLED = "cancelled";
    public static final String ENABLED = "enabled";
    public static final String DISABLED = "disabled";
    public static final String ACTIVE = "active";
    public static final String INACTIVE = "inactive";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String OTHER = "other";
    public static final String Y = "Y";
    public static final String N = "N";

    // 数字常量
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int HUNDRED = 100;
    public static final int THOUSAND = 1000;
    public static final int MILLION = 1000000;
    public static final int BILLION = 1000000000;

    public static final String ZERO_S = "0";
    public static final String ONE_S = "1";
    public static final String TWO_S = "2";

    public static final Long ZERO_L = 0L;
    public static final Long ONE_L = 1L;
    public static final Long TWO_L = 2L;

    // 超时时间
    public static final int DEFAULT_TIMEOUT = 5000; // 5秒
    public static final int DEFAULT_RETRY_TIMES = 3;
    public static final int DEFAULT_THREAD_POOL_SIZE = 10;
    public static final int DEFAULT_QUEUE_CAPACITY = 1000;
    public static final int DEFAULT_MAX_POOL_SIZE = 50;
    public static final int DEFAULT_KEEP_ALIVE_SECONDS = 60;


    public static final String PERCENT_CHAR = "%";
    public static final String LEFT_PARENTHESIS_CHAR = "(";
    public static final String RIGHT_PARENTHESIS_CHAR = ")";
    public static final String ESCAPE_CHAR = "\\";
    public static final String CURLY_LEFT = "{";
    public static final String CURLY_RIGHT = "}";
    public static final String COMMA_CHAR = ",";
    public static final String DOUBLE_QUOTE_CHAR = "'";
    public static final String SINGLE_QUOTE_CHAR = "'";
    public static final String COLON_CHAR = ":";
    public static final String DASH_CHAR = "-";
    public static final String DEFAULT_VALUE_SEPARATOR = ":-";
}
