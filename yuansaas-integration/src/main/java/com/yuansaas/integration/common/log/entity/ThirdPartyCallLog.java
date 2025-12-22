package com.yuansaas.integration.common.log.entity;

import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@Entity
@Table(name = "third_party_call_log")
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyCallLog extends BaseEntity {

    /**
     * 链路追踪
     */
    private String traceId;

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 接口或方法名称
     */
    private String action;

    /**
     * 调用方式: URI / SDK
     */
    private String callType;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 响应状态码
     */
    private String responseStatus;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 调用耗时(ms)
     */
    private Long durationMs;

    /**
     * 'SUCCESS / ERROR'
     */
    private String result;

    /**
     * 尝试次数
     */
    private Integer attempt;

    /**
     * 错误信息
     */
    private String errorMsg;

}