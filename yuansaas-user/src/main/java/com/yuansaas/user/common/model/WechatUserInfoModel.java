package com.yuansaas.user.common.model;

import com.yuansaas.user.common.enums.UserWxAuthClient;
import lombok.Builder;
import lombok.Data;

/**
 * 微信用户
 *
 * @author HTB 2025/8/8 15:33
 */
@Data
@Builder
public class WechatUserInfoModel {

    /**
     * 微信客户端类型
     */
    private UserWxAuthClient wxClient;

    /**
     * 微信唯一标识
     */
    private String openid;

    /**
     * 微信唯一标识
     */
    private String unionId;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信头像
     */
    private String avatar;
}
