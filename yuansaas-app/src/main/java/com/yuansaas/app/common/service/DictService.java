package com.yuansaas.app.common.service;

import com.yuansaas.app.common.params.FindDictParam;
import com.yuansaas.app.common.params.SaveDictParam;
import com.yuansaas.app.common.params.UpdateDictParam;
import com.yuansaas.app.common.params.UpdateSortParam;
import com.yuansaas.app.common.vo.SysDictTypeVo;
import com.yuansaas.core.page.RPage;


/**
 *
 * 字典配置
 *
 * @author LXZ 2025/11/17 18:06
 */
public interface DictService {

    /**
     * 创建字典数据
     * @param saveDictParam 创建字典相关参数
     * @author  lxz 2025/11/16 14:35
     */
    Boolean createDict(SaveDictParam saveDictParam);

    /**
     * 编辑字典
     * @param updateDictParam 编辑字典相关参数
     * @author  lxz 2025/11/16 14:35
     */
    Boolean updateDict(UpdateDictParam updateDictParam);

    /**
     * 修改字典排序
     * @param updateSortParam 字典数据参数
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
     * 根据查询条件返回字典列表
     * @param findDictParam 查询条件参数
     * @author  lxz 2025/11/16 14:35
     */
    RPage<SysDictTypeVo> getDictListByPage(FindDictParam findDictParam);
}
