package com.yuansaas.user.common.repository;

import com.yuansaas.user.common.entity.UserWechatBinding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 用户微信绑定
 *
 * @author HTB 2025/7/31 16:20
 */
public interface UserWechatBindingRepository extends JpaRepository<UserWechatBinding, Long> {

    Optional<UserWechatBinding> findByOpenid(String openid);
}
