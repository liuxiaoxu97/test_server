package com.yuansaas.user.common.service;

import com.yuansaas.user.common.entity.UserWechatBinding;
import com.yuansaas.user.common.enums.UserType;
import com.yuansaas.user.common.model.WechatUserInfoModel;

/**
 * 用户绑定微信记录
 *
 * @author HTB 2025/7/31 16:21
 */
public interface WechatBindingService {

    /**
     * 获取微信用户信息
     * @param code 微信授权code
     * @return 微信用户信息
     */
    WechatUserInfoModel getUserInfo(String code);

    /**
     * 获取微信用户绑定信息
     * @param openid 微信openid
     * @return 微信用户绑定信息
     */
    UserWechatBinding findBindingByOpenid(String openid);

    /**
     * 绑定微信用户
     * @param userId 用户id
     * @param userType 用户类型
     * @param wechatUser 微信用户信息
     */
    void bindWechat(Long userId, UserType userType, WechatUserInfoModel wechatUser);
}
