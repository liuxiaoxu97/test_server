package com.yuansaas.app.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yuansaas.app.shop.entity.ShopDataConfig;
import com.yuansaas.app.shop.param.UpdateShopDataParam;
import com.yuansaas.app.shop.repository.ShopDataConfigRepository;
import com.yuansaas.app.shop.repository.ShopRepository;
import com.yuansaas.app.shop.service.ShopDataService;
import com.yuansaas.app.shop.service.mapstruct.ShopMapStruct;
import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.jackson.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 商家基本配置服务实现类
 *
 * @author LXZ 2025/12/21 18:50
 */
@Service
@RequiredArgsConstructor
public class ShopDataServiceImpl implements ShopDataService {


    private final ShopRepository shopRepository;
    private final ShopDataConfigRepository shopDataConfigRepository;
    private final ShopMapStruct shopMapStruct;

    /**
     * 编辑商家基本信息
     *
     * @param updateShopDataParam 商家参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    @Transactional
    public Boolean update(UpdateShopDataParam updateShopDataParam) {
        shopRepository.findById(updateShopDataParam.getId()).ifPresentOrElse( shop -> {
            // 更新商家信息
            shop.setName(updateShopDataParam.getName());
            shop.setType(updateShopDataParam.getType().name());
            shop.setProvinceCode(updateShopDataParam.getAddress().getProvinceCode());
            shop.setCityCode(updateShopDataParam.getAddress().getCityCode());
            shop.setDistrictCode(updateShopDataParam.getAddress().getDistrictCode());
            shop.setAddress(updateShopDataParam.getAddress());
            shop.setUpdateBy(AppContextUtil.getUserInfo());
            shop.setUpdateAt(LocalDateTime.now());
            shopRepository.save(shop);
            // 更新商家基本配置信息
            ShopDataConfig byShopCode = shopDataConfigRepository.findByShopCode(shop.getCode());
            ShopDataConfig shopDataConfig = null;
            if (ObjectUtil.isEmpty(byShopCode)) {
               shopDataConfig = shopMapStruct.toShopDataConfig(updateShopDataParam, shop);
            } else {
                // 更新
                shopDataConfig = shopMapStruct.toShopDataConfig(updateShopDataParam, shop);

            }
            shopDataConfigRepository.save(shopDataConfig);

        } ,()->{
            throw DataErrorCode.DATA_NOT_FOUND.buildException("商家不存在");
        });
        return true;
    }




}
