package com.yuansaas.user.client.repository;

import com.yuansaas.user.client.entity.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 客户端用户
 *
 * @author HTB 2025/7/31 16:23
 */
public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {

    Optional<ClientUser> findByUserName(String username);
}
