package com.yuansaas.app.user.common.service;

import com.yuansaas.app.user.common.vo.WxMpQrCodeTicketVo;
import com.yuansaas.user.common.enums.UserType;

/**
 * 授权登录
 *
 * @author HTB 2025/8/22 11:04
 */
public interface AuthService {

    /**
     * 获取临时二维码ticket
     * @param userType 用户类型
     * @return 临时二维码ticket
     */
    WxMpQrCodeTicketVo getTempQrCodeTicket(UserType userType);

}
