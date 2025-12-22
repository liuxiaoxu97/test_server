package com.yuansaas.user.common.service.impl;

import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.user.common.entity.UserWechatBinding;
import com.yuansaas.user.common.enums.UserType;
import com.yuansaas.user.common.model.WechatUserInfoModel;
import com.yuansaas.user.common.repository.UserWechatBindingRepository;
import com.yuansaas.user.common.service.WechatBindingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 用户绑定微信
 *
 * @author HTB 2025/8/5 09:42
 */
@Service
@RequiredArgsConstructor
public class WechatBindingServiceImpl implements WechatBindingService {

    private final UserWechatBindingRepository userWechatBindingRepository;
    @Override
    public WechatUserInfoModel getUserInfo(String code) {
        return null;
    }

    @Override
    public UserWechatBinding findBindingByOpenid(String openid) {
        return userWechatBindingRepository.findByOpenid(openid).orElse( null);
    }

    @Override
    public void bindWechat(Long userId, UserType userType, WechatUserInfoModel wechatUser) {
        if(ObjectUtils.isEmpty(userId)
                || ObjectUtils.isEmpty(userType)
                || ObjectUtils.isEmpty(wechatUser)
                || ObjectUtils.isEmpty(wechatUser.getOpenid())
                || ObjectUtils.isEmpty(wechatUser.getWxClient())){
            throw DataErrorCode.DATA_VALIDATION_FAILED.buildException("参数错误");
        }
        // 创建微信绑定记录
        UserWechatBinding userWechatBinding = new UserWechatBinding();
        userWechatBinding.setUserId(userId);
        userWechatBinding.setWxClient(wechatUser.getWxClient().name());
        userWechatBinding.setUserType(userType.name());
        userWechatBinding.setOpenid(wechatUser.getOpenid());
        userWechatBinding.setUnionid(wechatUser.getUnionId());
        userWechatBinding.setNickName(wechatUser.getNickname());
        userWechatBinding.setAvatar(wechatUser.getAvatar());
        userWechatBindingRepository.save(userWechatBinding);
    }
}
