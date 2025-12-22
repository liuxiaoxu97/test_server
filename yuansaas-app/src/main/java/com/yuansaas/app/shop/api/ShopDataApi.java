package com.yuansaas.app.shop.api;

import com.yuansaas.app.shop.param.*;
import com.yuansaas.app.shop.service.ShopDataService;
import com.yuansaas.app.shop.service.ShopService;
import com.yuansaas.app.shop.vo.ShopListVo;
import com.yuansaas.core.page.RPage;
import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 商家基本信息管理 API
 *
 * @author LXZ 2025/12/12 10:04
 */
@RequestMapping("/shop/data")
@RestController
@RequiredArgsConstructor
public class ShopDataApi {

    private final ShopDataService shopDataService;


    /**
     * 编辑商家基本信息
     * @param updateShopDataParam 商家参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> update(@RequestBody @Validated UpdateShopDataParam updateShopDataParam) {
        return ResponseBuilder.okResponse(shopDataService.update(updateShopDataParam));
    }


//    /**
//     * 设置商家营业时间
//     * @param updateShopDataParam 商家参数
//     * @author  lxz 2025/11/16 14:35
//     */
//    @RequestMapping(value = "/update",method = RequestMethod.POST)
//    @SecurityAuth
//    public ResponseEntity<ResponseModel<Boolean>> update(@RequestBody @Validated UpdateShopDataParam updateShopDataParam) {
//        return ResponseBuilder.okResponse(shopService.update(updateShopDataParam));
//    }
//
//
//    /**
//     * 设置商家预约规则配置
//     * @param updateShopDataParam 商家参数
//     * @author  lxz 2025/11/16 14:35
//     */
//    @RequestMapping(value = "/update",method = RequestMethod.POST)
//    @SecurityAuth
//    public ResponseEntity<ResponseModel<Boolean>> update(@RequestBody @Validated UpdateShopDataParam updateShopDataParam) {
//        return ResponseBuilder.okResponse(shopService.update(updateShopDataParam));
//    }

}
