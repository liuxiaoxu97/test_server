package com.yuansaas.app.shop.service;

import com.yuansaas.app.shop.param.*;
import com.yuansaas.app.shop.vo.ShopListVo;
import com.yuansaas.core.page.RPage;

/**
 *
 * 商家实现类
 *
 * @author LXZ 2025/12/12 10:52
 */
public interface ShopDataService {

    /**
     * 编辑商家基本信息
     * @param updateShopDataParam 商家参数
     * @author  lxz 2025/11/16 14:35
     */
    Boolean update(UpdateShopDataParam updateShopDataParam);

}
