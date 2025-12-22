package com.yuansaas.user.auth.param;

import com.yuansaas.user.auth.enums.LoginType;
import com.yuansaas.user.common.enums.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 统一登录参数
 *
 * @author HTB 2025/8/5 10:33
 */
@Data
public class UnifiedLoginParam {

    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    private UserType userType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码登录时必填
     */
    private String password;

    /**
     * 短信登录时必填
     */
    private String smsCode;

    /**
     * 微信登录时必填
     */
    private String wechatCode;

    /**
     * 微信公众号登录时必填
     * 关注公众号登录时，为二维码的sceneId
     */
    private String wechatMpSceneCode;

    /**
     * 登录方式
     */
    @NotNull(message = "登录方式不能为空")
    private LoginType loginType;

}
