package com.yuansaas.app.common.service;

import java.util.Map;

/**
 * 第三方消息参数
 *
 * @author HTB 2025/8/22 14:49
 */
public interface ThirdPartyMessageParamsService {

    /**
     * 创建配置, configKey为纯数字字串号，可转为数字类型
     * @param scene 配置场景
     * @param params 参数
     * @return 配置ID
     */
    String createNumberConfigKey(String scene , Map<String, Object> params);

    /**
     * 创建配置, configKey为String字串号
     * @param scene 配置场景
     * @param params 参数
     * @return 配置ID
     */
    String createStringConfigKey(String scene , Map<String, Object> params);

    /**
     * 获取参数配置（Map形式）
     */
    Map<String, Object> getAndValidateActiveParamsMapByKey(String configKey);

    /**
     * 禁用配置
     */
    void disableConfigByKey(String configKey);

}
