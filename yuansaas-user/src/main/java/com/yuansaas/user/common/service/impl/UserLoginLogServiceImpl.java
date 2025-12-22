package com.yuansaas.user.common.service.impl;

import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.utils.IpUtil;
import com.yuansaas.user.auth.enums.LoginType;
import com.yuansaas.user.auth.model.CustomUserDetails;
import com.yuansaas.user.common.entity.UserLoginLog;
import com.yuansaas.user.common.enums.UserType;
import com.yuansaas.user.common.repository.UserLoginLogRepository;
import com.yuansaas.user.common.service.UserLoginLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户登录日志
 *
 * @author HTB 2025/8/5 09:43
 */
@Service
@RequiredArgsConstructor
public class UserLoginLogServiceImpl implements UserLoginLogService {

    private final UserLoginLogRepository userLoginLogRepository;

    @Override
    public void logLogin(CustomUserDetails userDetails, LoginType loginType, String success, HttpServletRequest request) {
        UserLoginLog log = new UserLoginLog();
        log.setUserId(userDetails.getUserId());
        log.setUserType(userDetails.getUserType().name());
        log.setUserName(userDetails.getUsername());
        log.setLoginType(loginType.name());
        log.setTerminal(AppContextUtil.requireClientType());
        log.setCreateAt(LocalDateTime.now());
        buildRequestIfo(request, log);
        log.setSuccess(success);

        userLoginLogRepository.save(log);
    }

    @Override
    public void logLoginFailure(String username, UserType userType, LoginType loginType, HttpServletRequest request, String failureReason) {
        UserLoginLog log = new UserLoginLog();
        log.setUserName(username);
        log.setUserType(userType.name());
        log.setLoginType(loginType.name());
        log.setCreateAt(LocalDateTime.now());
        buildRequestIfo(request, log);
        log.setSuccess("FILE");
        log.setRemark(failureReason);

        userLoginLogRepository.save(log);
    }

    private String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    private void buildRequestIfo(HttpServletRequest request , UserLoginLog log){
        log.setRemoteIp(IpUtil.getClientIpAddr( request) );
        log.setDevice(getUserAgent(request));
    }
}
