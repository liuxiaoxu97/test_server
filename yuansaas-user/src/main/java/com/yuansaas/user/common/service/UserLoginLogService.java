package com.yuansaas.user.common.service;

import com.yuansaas.user.auth.enums.LoginType;
import com.yuansaas.user.auth.model.CustomUserDetails;
import com.yuansaas.user.common.enums.UserType;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户登录日志
 *
 * @author HTB 2025/7/31 16:21
 */
public interface UserLoginLogService {

    /**
     * 登录日志记录
     * @param userDetails 用户详情
     * @param loginType 登录类型
     * @param success 登录成功与否
     * @param request HTTP请求
     */
    void logLogin(CustomUserDetails userDetails, LoginType loginType,
                  String success, HttpServletRequest request);

    /**
     * 登录失败日志记录
     * @param username 用户名
     * @param userType 用户类型
     * @param loginType 登录类型
     * @param request HTTP请求
     * @param failureReason 登录失败原因
     */
    void logLoginFailure(String username, UserType userType, LoginType loginType,
                         HttpServletRequest request, String failureReason);


}
