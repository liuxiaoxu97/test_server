package com.yuansaas.app.common.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuansaas.app.common.entity.SysDictData;
import com.yuansaas.app.common.params.*;
import com.yuansaas.app.common.repository.SysDictDataRepository;
import com.yuansaas.app.common.repository.SysDictTypeRepository;
import com.yuansaas.app.common.service.DictItemService;
import com.yuansaas.app.common.service.DictService;
import com.yuansaas.app.common.vo.SysDictTypeVo;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.page.RPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 *
 * 字典数据配置
 *
 * @author LXZ 2025/11/20 10:57
 */
@Service
@RequiredArgsConstructor
public class DictItemServiceImpl implements DictItemService {

    private final JPAQueryFactory jpaQueryFactory;
    private final SysDictDataRepository sysDictDataRepository;
    private final SysDictTypeRepository sysDictTypeRepository;


    /**
     * 创建字典数据
     *
     * @param saveDictItemParam 创建字典相关参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean createDict(SaveDictItemParam saveDictItemParam) {
        sysDictTypeRepository.findById(saveDictItemParam.getDictTypeId()).orElseThrow(() -> DataErrorCode.DATA_NOT_FOUND.buildException("字典类型不存在"));

        SysDictData sysDictData = new SysDictData();
        BeanUtils.copyProperties(saveDictItemParam, sysDictData);
        sysDictData.setCreateAt(LocalDateTime.now());
        sysDictData.setCreateBy(AppContextUtil.getUserInfo());
        sysDictDataRepository.save(sysDictData);
        return true;
    }

    /**
     * 编辑字典
     *
     * @param updateDictItemParam 编辑字典相关参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean updateDict(UpdateDictItemParam updateDictItemParam) {
        sysDictDataRepository.findById(updateDictItemParam.getId());
        return null;
    }

    /**
     * 修改字典排序
     *
     * @param updateSortParam 修改字典排序
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean updateOrderNum(UpdateSortParam updateSortParam) {
        return null;
    }

    /**
     * 根据字典id删除字典数据
     *
     * @param id 字典id
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean deleteDict(Long id) {
        return null;
    }

    @Override
    public RPage<SysDictTypeVo> findByPage(FindDictParam findDictParam) {
        return null;
    }
}
