package com.yuansaas.app.common.api;

import com.yuansaas.app.common.params.*;
import com.yuansaas.app.common.service.DictItemService;
import com.yuansaas.app.common.service.DictService;
import com.yuansaas.app.common.vo.SysDictTypeVo;
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
 * 字典配置
 *
 * @author LXZ 2025/11/17 16:00
 */
@RestController
@RequestMapping(value = "/dict/item")
@RequiredArgsConstructor
public class DictItemApi {

    private final DictItemService dictItemService;

    /**
     * 创建字典数据
     * @param saveDictItemParam 创建字典相关参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> createDict(@RequestBody @Validated SaveDictItemParam saveDictItemParam) {
        return ResponseBuilder.okResponse(dictItemService.createDict(saveDictItemParam));
    }

    /**
     * 编辑字典
     * @param updateDictItemParam 编辑字典相关参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> updateDict(@RequestBody @Validated UpdateDictItemParam updateDictItemParam) {
        return ResponseBuilder.okResponse(dictItemService.updateDict(updateDictItemParam));
    }


    /**
     * 修改字典排序
     * @param updateSortParam 修改字典排序
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/update/order_num",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> updateOrderNum(@RequestBody @Validated UpdateSortParam updateSortParam) {
        return ResponseBuilder.okResponse(dictItemService.updateOrderNum(updateSortParam));
    }

    /**
     * 根据字典id删除字典数据
     * @param id 字典id
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> deleteDict(@PathVariable(value = "id") Long id) {
        return ResponseBuilder.okResponse(dictItemService.deleteDict(id));
    }

    /**
     * 查询字典项分页
     * @param findDictParam 字典id
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @SecurityAuth
    public ResponseEntity<ResponseModel<RPage<SysDictTypeVo>>> findByPage(FindDictParam findDictParam) {
        return ResponseBuilder.okResponse(dictItemService.findByPage(findDictParam));
    }


}
