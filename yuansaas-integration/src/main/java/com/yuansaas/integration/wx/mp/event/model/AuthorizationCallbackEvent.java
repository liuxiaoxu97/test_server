package com.yuansaas.integration.wx.mp.event.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网页授权回调
 *
 * @author HTB 2025/8/21 19:07
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationCallbackEvent {

    /**
     * 公众号ID
     */
    private String mpAppId;

    /**
     * openid普通用户的标识，对当前开发者帐号唯一
     */
    private String openid;
    /**
     * nickname	普通用户昵称
     */
    private String nickname;
    /**
     * sex	普通用户性别，1为男性，2为女性
     */
    private Integer sex;
    /**
     * city	普通用户个人资料填写的城市
     */
    private String city;

    /**
     * province	普通用户个人资料填写的省份
     */
    private String province;

    /**
     * country	国家，如中国为CN
     */
    private String country;

    /**
     * headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空
     */
    @SerializedName("headimgurl")
    private String headImgUrl;

    /**
     * unionid	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    @SerializedName("unionid")
    private String unionId;

}
