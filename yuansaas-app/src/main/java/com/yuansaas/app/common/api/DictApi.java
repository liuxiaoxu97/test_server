package com.yuansaas.app.common.api;

import com.google.common.collect.Lists;
import com.yuansaas.app.common.params.FindDictParam;
import com.yuansaas.app.common.params.SaveDictParam;
import com.yuansaas.app.common.params.UpdateDictParam;
import com.yuansaas.app.common.params.UpdateSortParam;
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

import java.util.List;

/**
 *
 * 字典配置
 *
 * @author LXZ 2025/11/17 16:00
 */
@RestController
@RequestMapping(value = "/dict")
@RequiredArgsConstructor
public class DictApi {

    private final DictService dictService;

    /**
     * 创建字典项
     * @param saveDictParam 创建字典相关参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> createDict(@RequestBody @Validated SaveDictParam saveDictParam) {
        return ResponseBuilder.okResponse(dictService.createDict(saveDictParam));
    }

    /**
     * 编辑字典项
     * @param updateDictParam 编辑字典相关参数
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> updateDict(@RequestBody @Validated UpdateDictParam updateDictParam) {
        return ResponseBuilder.okResponse(dictService.updateDict(updateDictParam));
    }


    /**
     * 修改字典项排序
     * @param updateSortParam 修改字典排序
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/update/order_num",method = RequestMethod.POST)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> updateOrderNum(@RequestBody @Validated UpdateSortParam updateSortParam) {
        return ResponseBuilder.okResponse(dictService.updateOrderNum(updateSortParam));
    }

    /**
     * 根据字典id删除字典项
     * @param id 字典id
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @SecurityAuth
    public ResponseEntity<ResponseModel<Boolean>> deleteDict(@PathVariable(value = "id") Long id) {
        return ResponseBuilder.okResponse(dictService.deleteDict(id));
    }

    /**
     * 查询字典项分页
     * @param findDictParam 字典id
     * @author  lxz 2025/11/16 14:35
     */
    @RequestMapping(value = "/page",method = RequestMethod.DELETE)
    @SecurityAuth
    public ResponseEntity<ResponseModel<RPage<SysDictTypeVo>>> findByPage(FindDictParam findDictParam) {
        return ResponseBuilder.okResponse(dictService.getDictListByPage(findDictParam));
    }


}
