package com.yuansaas.app.user.common.listener.auth;

import com.yuansaas.core.redis.RedisUtil;
import com.yuansaas.integration.wx.mp.event.model.SubscribeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 关注微信公众号事件
 * @apiNote 该事件来源于easeVista-integration模块，当用户关注/取消公众号时出发该事件
 * @author HTB 2025/8/22 15:43
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class WxMpBasicListener {


    /**
     * 关注公众号事件
     * 如果参数中有sceneId，则代表是扫描带参数二维码进来的，用于后续登录，如果要在具体区分可根据sceneId在MessageParam表中获取该场景值对应的具体信息由于具体业务逻辑处理，否则是普通关注事件
     */
    @EventListener
    public void subscribe(SubscribeEvent event) {
        log.info("关注公众号事件: {}", event);
        if(StringUtils.isNotBlank(event.getSceneId())){
            // 获取场景参数 sceneId 对应的具体信息, 将信息保存在数据库并保证到redis中，用于前端轮训查询关注信息
            RedisUtil.set(RedisUtil.genKey("WX_MP_SUBSCRIBE_INFO" , event.getSceneId()), event , 5 , TimeUnit.MINUTES);
        }else {
            // 普通关注
        }

    }

    /**
     * 取消关注公众号事件
     */
//    @EventListener
//    public void unsubscribe(SubscribeEvent event) {
//        log.info("关注公众号事件: {}", event);
//    }

}
