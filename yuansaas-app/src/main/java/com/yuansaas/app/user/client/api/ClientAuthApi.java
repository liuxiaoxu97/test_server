package com.yuansaas.app.user.client.api;

import com.yuansaas.app.user.common.service.AuthService;
import com.yuansaas.app.user.common.vo.WxMpQrCodeTicketVo;
import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.common.enums.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端用户授权登录
 *
 * @author HTB 2025/8/22 10:54
 */
@Slf4j
@RestController("user/client/auth")
@RequiredArgsConstructor
public class ClientAuthApi {

    private final AuthService authService;
    /**
     * 获取临时二维码授权码
     *
     * @return WxMpQrCodeTicketVo
     */
    @RequestMapping("/get/temp/qc/code/ticket")
    public ResponseEntity<ResponseModel<WxMpQrCodeTicketVo>> getTempQcCodeTicket(){
        return ResponseBuilder.okResponse(authService.getTempQrCodeTicket(UserType.CLIENT_USER));
    }


}
