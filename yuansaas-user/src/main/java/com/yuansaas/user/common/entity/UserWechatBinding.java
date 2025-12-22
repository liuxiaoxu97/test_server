package com.yuansaas.user.common.entity;

import com.yuansaas.core.jpa.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "user_wechat_binding")
public class UserWechatBinding extends BaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 微信客户端(XXX小程序\XXX公众号)
     */
    private String wxClient;

    /**
     * 微信OpenID
     */
    private String openid;

    /**
     * 微信UnionID
     */
    private String unionid;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 微信头像
     */
    private String avatar;

}