package com.yuansaas.app.common.service.impl;

import com.yuansaas.app.common.entity.ThirdPartyMessageParams;
import com.yuansaas.app.common.repository.ThirdPartyMessageParamsRepository;
import com.yuansaas.app.common.service.ThirdPartyMessageParamsService;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.utils.id.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * 第三方消息参数
 *
 * @author HTB 2025/8/22 14:49
 */
@Service
@RequiredArgsConstructor
public class ThirdPartyMessageParamsServiceImpl implements ThirdPartyMessageParamsService {

    private final ThirdPartyMessageParamsRepository messageParamsRepository;
    private final SnowflakeIdGenerator idGenerator;

    @Transactional
    @Override
    public String createNumberConfigKey(String scene, Map<String, Object> params) {
        String nextId = String.valueOf(idGenerator.nextId());
        createConfig(scene, params, nextId);
        return nextId;
    }

    @Transactional
    @Override
    public String createStringConfigKey(String scene, Map<String, Object> params) {
        String next = RandomStringUtils.secure().next(16);
        createConfig(scene, params, next);
        return next;
    }

    @Override
    public Map<String, Object> getAndValidateActiveParamsMapByKey(String configKey) {
        Optional<ThirdPartyMessageParams> messageParamsOptional = messageParamsRepository.findByConfigKey(configKey);
        if (messageParamsOptional.isPresent()) {
            ThirdPartyMessageParams messageParams = messageParamsOptional.get();
            if(messageParams.getStatus()){
                return messageParams.getParams();
            }
            throw DataErrorCode.DATA_DISABLED.buildException();
        }
        return null;
    }

    @Transactional
    @Override
    public void disableConfigByKey(String configKey) {
        messageParamsRepository.disableConfig(configKey , LocalDateTime.now() , AppContextUtil.getUserInfo());
    }

    /**
     * 创建配置
     * @param scene 场景
     * @param params 参数
     * @param configKey 配置key
     */
    private void createConfig(String scene, Map<String, Object> params , String configKey) {
        if (messageParamsRepository.findByConfigKey(configKey).isPresent()) {
            throw DataErrorCode.DATA_ALREADY_EXISTS.buildException("Config Key already exists.");
        }
        ThirdPartyMessageParams messageParams = new ThirdPartyMessageParams();
        messageParams.setScene(scene);
        messageParams.setParams(params);
        messageParams.setConfigKey(configKey);
        messageParams.setCreateBy(AppContextUtil.getUserInfo());
        messageParams.setCreateAt(LocalDateTime.now());
        messageParamsRepository.save(messageParams);
    }

}
