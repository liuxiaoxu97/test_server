package com.yuansaas.app.common.service;

import com.yuansaas.app.common.params.*;
import com.yuansaas.app.common.vo.SysDictTypeVo;
import com.yuansaas.core.page.RPage;
import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * 字典数据配置
 *
 * @author LXZ 2025/11/20 10:52
 */
public interface DictItemService {

    /**
     * 创建字典数据
     * @param saveDictItemParam 创建字典相关参数
     * @author  lxz 2025/11/16 14:35
     */
    Boolean createDict(SaveDictItemParam saveDictItemParam);

    /**
     * 编辑字典
     * @param updateDictItemParam 编辑字典相关参数
     * @author  lxz 2025/11/16 14:35
     */

    Boolean updateDict(UpdateDictItemParam updateDictItemParam);


    /**
     * 修改字典排序
     * @param updateSortParam 修改字典排序
     * @author  lxz 2025/11/16 14:35
     */
    Boolean updateOrderNum(UpdateSortParam updateSortParam);

    /**
     * 根据字典id删除字典数据
     * @param id 字典id
     * @author  lxz 2025/11/16 14:35
     */
    Boolean deleteDict(Long id);

    /**
     * 查询字典项分页
     * @param findDictParam 字典id
     * @author  lxz 2025/11/16 14:35
     */
    ;RPage<SysDictTypeVo> findByPage(FindDictParam findDictParam);
}
