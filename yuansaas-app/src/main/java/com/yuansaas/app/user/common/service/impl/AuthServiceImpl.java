package com.yuansaas.app.user.common.service.impl;

import com.google.common.collect.Maps;
import com.yuansaas.app.common.service.ThirdPartyMessageParamsService;
import com.yuansaas.app.user.common.service.AuthService;
import com.yuansaas.app.user.common.vo.WxMpQrCodeTicketVo;
import com.yuansaas.integration.wx.mp.service.WxMpAuthService;
import com.yuansaas.user.common.enums.UserType;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 授权登录
 *
 * @author HTB 2025/8/22 11:08
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ThirdPartyMessageParamsService thirdPartyMessageParamsService;
    private final WxMpAuthService wxMpAuthService;

    @Override
    public WxMpQrCodeTicketVo getTempQrCodeTicket(UserType userType) {
        HashMap<@Nullable String, @Nullable Object> dataMap = Maps.newHashMap();
        dataMap.put("userType" , userType.getName());
        String numberConfigKey = thirdPartyMessageParamsService.createNumberConfigKey("wx_temp_qrcode_ticket", dataMap);
        // 由于是授权登录。所以二维码码有效期是5分钟
        WxMpQrCodeTicket tempQrCodeTicket = wxMpAuthService.getTempQrCodeTicket(Integer.parseInt(numberConfigKey), 5 * 60);
        return WxMpQrCodeTicketVo.builder()
                .ticket(tempQrCodeTicket.getTicket())
                .expireSeconds(tempQrCodeTicket.getExpireSeconds())
                .sceneId(numberConfigKey)
                .build();
    }


}
