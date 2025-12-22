package com.yuansaas.user.client.service;

import com.yuansaas.user.client.entity.ClientUser;

import java.util.Optional;

/**
 * 客户端用户接口
 *
 * @author HTB 2025/7/31 16:24
 */
public interface ClientUserService {

    /**
     * 通过id查询客户端用户
     * @param id  id
     * @return 客户端用户
     */
    Optional<ClientUser> findById(Long id);

    /**
     * 通过用户名查询客户端用户
     * @param username 用户名
     * @return 客户端用户
     */
    Optional<ClientUser> findByUsername(String username);

    /**
     * 保存客户端用户
     * @param clientUser 客户端用户
     * @return 客户端用户
     */
    ClientUser save(ClientUser clientUser);

    /**
     * 锁定用户
     * @param userId 用户id
     */
    void lockUser(Long userId);

    /**
     * 解锁用户
     * @param userId 用户id
     */
    void unlockUser(Long userId);
}
