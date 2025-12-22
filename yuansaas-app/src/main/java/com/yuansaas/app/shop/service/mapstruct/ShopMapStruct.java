package com.yuansaas.app.shop.service.mapstruct;

import com.yuansaas.app.shop.entity.Shop;
import com.yuansaas.app.shop.entity.ShopDataConfig;
import com.yuansaas.app.shop.param.SaveShopParam;
import com.yuansaas.app.shop.param.UpdateShopDataParam;
import com.yuansaas.app.shop.param.UpdateShopParam;
import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.core.context.AppContext;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.jackson.JacksonUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *
 *
 * @author LXZ 2025/12/12 10:57
 */
@Component
public class ShopMapStruct {



    public Shop toSaveShop (SaveShopParam shopParam) {
        Shop shop = new Shop();
        shop.setName(shopParam.getName());
        shop.setType(shopParam.getType().name());
        shop.setProvinceCode(shopParam.getAddress().getProvinceCode());
        shop.setCityCode(shopParam.getAddress().getCityCode()   );
        shop.setDistrictCode(shopParam.getAddress().getDistrictCode());
        shop.setAddress(shopParam.getAddress());
        shop.setLegalPersonName(shopParam.getLegalPersonName());
        shop.setLegalPersonPhone(shopParam.getLegalPersonPhone());
        shop.setLegalPersonEmail(shopParam.getLegalPersonEmail());
        shop.setUnifiedCreditCode(shopParam.getUnifiedCreditCode());
        shop.setIdCardFront(shopParam.getIdCardFront());
        shop.setIdCardBack(shopParam.getIdCardBack());
        shop.setBusinessLicense(shopParam.getBusinessLicense());
        shop.setSignedStatus(shopParam.getSignedStatus());
        shop.setLockStatus(AppConstants.N);
        shop.setDeleteStatus(AppConstants.N);
        shop.setCreateBy(AppContextUtil.getUserInfo());
        shop.setCreateAt(LocalDateTime.now());
        return shop;
    }

    public void toUpdateShop ( Shop shop , UpdateShopParam shopParam) {
        shop.setName(shopParam.getName());
        shop.setProvinceCode(shopParam.getAddress().getProvinceCode());
        shop.setCityCode(shopParam.getAddress().getCityCode()   );
        shop.setDistrictCode(shopParam.getAddress().getDistrictCode());
        shop.setAddress(shopParam.getAddress());
        shop.setLegalPersonName(shopParam.getLegalPersonName());
        shop.setLegalPersonPhone(shopParam.getLegalPersonPhone());
        shop.setLegalPersonEmail(shopParam.getLegalPersonEmail());
        shop.setUnifiedCreditCode(shopParam.getUnifiedCreditCode());
        shop.setIdCardFront(shopParam.getIdCardFront());
        shop.setIdCardBack(shopParam.getIdCardBack());
        shop.setBusinessLicense(shopParam.getBusinessLicense());
        shop.setSignedStatus(shop.getSignedStatus());
        shop.setLockStatus(AppConstants.N);
        shop.setDeleteStatus(AppConstants.N);
        shop.setCreateBy(AppContextUtil.getUserInfo());
        shop.setCreateAt(LocalDateTime.now());
    }

    public ShopDataConfig toShopDataConfig (UpdateShopDataParam shopDataParam, Shop shop) {
        ShopDataConfig shopDataConfig = new ShopDataConfig();
        shopDataConfig.setShopCode(shop.getCode());
        shopDataConfig.setIntro(shopDataParam.getIntro());
        shopDataConfig.setLabel(JacksonUtil.toJson(shopDataParam.getLabel()));
        shopDataConfig.setPhone(shopDataParam.getPhone());
        shopDataConfig.setSubjectColor(shopDataParam.getSubjectColor());
        shopDataConfig.setLogo(shopDataParam.getLogo());
        shopDataConfig.setCustomerServiceWechat(shopDataParam.getCustomerServiceWechat());
        shopDataConfig.setOfficialAccounts(shopDataParam.getOfficialAccounts());
        shopDataConfig.setCreateAt(LocalDateTime.now());
        shopDataConfig.setCreateBy(AppContextUtil.getUserInfo());
        shopDataConfig.setUpdateAt(LocalDateTime.now());
        shopDataConfig.setUpdateBy(AppContextUtil.getUserInfo());
        return shopDataConfig;
    }
}
