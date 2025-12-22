package com.yuansaas.app.shop.service;

import com.yuansaas.app.shop.param.FindShopParam;
import com.yuansaas.app.shop.param.SaveShopParam;
import com.yuansaas.app.shop.param.SignedParam;
import com.yuansaas.app.shop.param.UpdateShopParam;
import com.yuansaas.app.shop.vo.ShopListVo;
import com.yuansaas.core.page.RPage;
import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * 商家实现类
 *
 * @author LXZ 2025/12/12 10:52
 */
public interface ShopService {

    /**
     * 添加商家
     * @param saveShopParam 商家参数
     * @author  lxz 2025/11/16 14:35
     */
    Boolean add(SaveShopParam saveShopParam);

    /**
     * 编辑商家
     * @param updateShopParam 商家参数
     * @author  lxz 2025/11/16 14:35
     */
    Boolean update(UpdateShopParam updateShopParam);

    /**
     * 禁用商家
     * @param id 商家id
     * @author  lxz 2025/11/16 14:35
     */
    Boolean lock(Long id);

    /**
     * 删除商家
     * @param id 商家id
     * @author  lxz 2025/11/16 14:35
     */
    Boolean delete(Long id);

    /**
     * 查询商家列表
     * @param findShopParam 查询参数
     * @return 商家列表
     * @author  lxz 2025/11/16 14:35
     */
    RPage<ShopListVo> getByPage(FindShopParam findShopParam);
    /**
     * 签约操作
     * @param signedParam 签约参数
     * @author  lxz 2025/11/16 14:35
     */
    Boolean signed(SignedParam signedParam);
}
