package com.yuansaas.user.client.service.impl;

import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.user.client.entity.ClientUser;
import com.yuansaas.user.client.repository.ClientUserRepository;
import com.yuansaas.user.client.service.ClientUserService;
import com.yuansaas.user.common.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 客户端接口
 *
 * @author HTB 2025/8/8 14:46
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ClientUserServiceImpl implements ClientUserService {

    private final ClientUserRepository  clientUserRepository;

    @Override
    public Optional<ClientUser> findById(Long id) {
        return clientUserRepository.findById(id);
    }

    @Override
    public Optional<ClientUser> findByUsername(String username) {
        return clientUserRepository.findByUserName(username);
    }

    @Override
    public ClientUser save(ClientUser clientUser) {
        clientUser.setRegistrationAt(LocalDateTime.now());
        clientUser.setCreateBy(AppContextUtil.getUserInfo());
        clientUser.setCreateAt(LocalDateTime.now());
        return clientUserRepository.save(clientUser);
    }

    @Override
    public void lockUser(Long userId) {
        clientUserRepository.findById(userId)
                .ifPresentOrElse(clientUser -> {
                    clientUser.setStatus(UserStatus.suspended.name());
                    clientUser.setUpdateBy(AppContextUtil.getUserInfo());
                    clientUser.setUpdateAt(LocalDateTime.now());
                    clientUserRepository.save(clientUser);
                }, () -> {
                    throw DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
                });
    }

    @Override
    public void unlockUser(Long userId) {
        clientUserRepository.findById(userId)
                .ifPresentOrElse(clientUser -> {
                     clientUser.setStatus(UserStatus.active.name());
                     clientUser.setUpdateBy(AppContextUtil.getUserInfo());
                     clientUser.setUpdateAt(LocalDateTime.now());
                     clientUserRepository.save(clientUser);
                 }, () -> {
                     throw DataErrorCode.DATA_NOT_FOUND.buildException("用户不存在");
                 });
    }
}
