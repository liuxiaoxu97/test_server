package com.yuansaas.core.response;

import lombok.*;

/**
 * 相应model
 *
 * @author HTB 2025/7/21 16:56
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseModel<T> {

    /**
     * 状态码
     * - 成功： SUCCESS 0
     * - 业务失败：ERROR 4XX
     * - 系统异常：FAIL 500
     */
    private String status;

    /**
     * 业务状态码
     * - 0: 全局成功状态
     * - 1xxx: 用户相关错误
     * - 2xxx: 参数验证错误
     * - 3xxx: 业务逻辑错误
     * - 4xx/5xx: 标准HTTP状态码
     */
    private String code;

    /**
     *  数据
     */
    private T data;

    /**
     * 链路追踪ID
     */
    private String traceId;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 时间戳
     */
    @Builder.Default
    private long timestamp = System.currentTimeMillis();

    /**
     * 调试信息
     */
    private String debug;

}
