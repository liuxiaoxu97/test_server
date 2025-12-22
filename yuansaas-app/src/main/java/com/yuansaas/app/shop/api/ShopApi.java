package com.yuansaas.app.shop.api;

import com.yuansaas.app.shop.param.FindShopParam;
import com.yuansaas.app.shop.param.SaveShopParam;
import com.yuansaas.app.shop.param.SignedParam;
import com.yuansaas.app.shop.param.UpdateShopParam;
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
 * 商家管理 API
 *
 * @author LXZ 2025/12/12 10:04
 */
@RequestMapping("/shop")
@RestController
@RequiredArgsConstructor
public class ShopApi {

    private final ShopService shopService;


    /**
     * 添加商家
     * @param saveShopParam 商家参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> add(@RequestBody @Validated SaveShopParam saveShopParam) {
        return ResponseBuilder.okResponse(shopService.add(saveShopParam));
    }

    /**
     * 编辑商家
     * @param updateShopParam 商家参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> update(@RequestBody @Validated UpdateShopParam updateShopParam) {
        return ResponseBuilder.okResponse(shopService.update(updateShopParam));
    }

    /**
     * 禁用商家
     * @param id 商家id
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/lock/{id}",method = RequestMethod.PUT)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> lock(@PathVariable(value = "id") Long id) {
        return ResponseBuilder.okResponse(shopService.lock(id));
    }

    /**
     * 删除商家
     * @param id 商家id
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> delete(@PathVariable(value = "id") Long id) {
        return ResponseBuilder.okResponse(shopService.delete(id));
    }

    /**
     * 查询商家列表
     * @param findShopParam 查询参数
     * @return 商家列表
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @SecurityAuth
    public ResponseEntity<ResponseModel<RPage<ShopListVo>>> getByPage( FindShopParam findShopParam) {
        return ResponseBuilder.okResponse(shopService.getByPage(findShopParam));
    }


    /**
     * 签约操作
     * @param signedParam 签约参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/signed",method = RequestMethod.PUT)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> signed(@RequestBody  @Validated SignedParam signedParam) {
        return ResponseBuilder.okResponse(shopService.signed(signedParam));
    }
}
