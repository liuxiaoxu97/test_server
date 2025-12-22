package com.yuansaas.app.common.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuansaas.app.common.entity.QSysDictType;
import com.yuansaas.app.common.entity.SysDictType;
import com.yuansaas.app.common.params.FindDictParam;
import com.yuansaas.app.common.params.SaveDictParam;
import com.yuansaas.app.common.params.UpdateDictParam;
import com.yuansaas.app.common.params.UpdateSortParam;
import com.yuansaas.app.common.repository.SysDictTypeRepository;
import com.yuansaas.app.common.service.DictService;
import com.yuansaas.app.common.vo.SysDictTypeVo;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.jpa.querydsl.BoolBuilder;
import com.yuansaas.core.page.RPage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 字典服务实现类
 *
 * @author LXZ 2025/11/18 09:13
 */
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final SysDictTypeRepository dictRepository;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 创建字典数据
     *
     * @param saveDictParam 创建字典相关参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean createDict(SaveDictParam saveDictParam) {
        SysDictType dict = new SysDictType();
        BeanUtils.copyProperties(saveDictParam, dict);
        dict.setCreateBy(AppContextUtil.getUserInfo());
        dict.setCreateAt(LocalDateTime.now());
        dictRepository.save(dict);
        return true;
    }

    /**
     * 编辑字典
     *
     * @param updateDictParam 编辑字典相关参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean updateDict( UpdateDictParam updateDictParam) {
        // 查询字典数据
        SysDictType sysDictType = dictRepository.findById(updateDictParam.getId()).orElse(null);
        if (ObjectUtil.isEmpty(sysDictType)) {
            throw DataErrorCode.DATA_VALIDATION_FAILED.buildException("字典数据不存在");
        }
        // 更新字典数据
        sysDictType = new SysDictType();
        sysDictType.setDictName(updateDictParam.getDictName());
        sysDictType.setDictType(updateDictParam.getDictType());
        sysDictType.setPlatform(updateDictParam.getPlatform());
        sysDictType.setSort(updateDictParam.getSort());
        sysDictType.setUpdateBy(AppContextUtil.getUserInfo());
        sysDictType.setUpdateAt(LocalDateTime.now());
        dictRepository.save(sysDictType);
        return true;
    }

    /**
     * 修改字典排序
     *
     * @param updateSortParam 字典数据添加参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean updateOrderNum(UpdateSortParam updateSortParam) {
        SysDictType sysDictType = dictRepository.findById(updateSortParam.getId()).orElse(null);
        if (ObjectUtil.isEmpty(sysDictType)) {
            throw DataErrorCode.DATA_VALIDATION_FAILED.buildException("字典数据不存在");
        }
        sysDictType.setSort(updateSortParam.getSort());
        sysDictType.setUpdateBy(AppContextUtil.getUserInfo());
        sysDictType.setUpdateAt(LocalDateTime.now());
        dictRepository.save(sysDictType);
        return true;
    }

    /**
     * 根据字典id删除字典数据
     *
     * @param id 字典id
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean deleteDict(Long id) {
        dictRepository.deleteById(id);
        return true;
    }

    /**
     * 根据查询条件返回字典列表
     * @param findDictParam 查询条件参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public RPage<SysDictTypeVo> getDictListByPage(FindDictParam findDictParam) {
        // 查询条件
        QSysDictType qSysDictType = QSysDictType.sysDictType;
        QueryResults<SysDictTypeVo> pageDict = jpaQueryFactory.select(Projections.bean(SysDictTypeVo.class,
                        qSysDictType.id,
                        qSysDictType.dictName,
                        qSysDictType.dictType,
                        qSysDictType.platform,
                        qSysDictType.sort,
                        qSysDictType.updateBy
                )).from(qSysDictType)
                .where(BoolBuilder.getInstance()
                        .and(findDictParam.getDictName(), qSysDictType.dictName::contains)
                        .and(findDictParam.getPlatform(), qSysDictType.platform::eq)
                        .getWhere())
                .offset(findDictParam.obtainOffset())
                .limit(findDictParam.getPageSize())
                .fetchResults();
        return new RPage<>(findDictParam.getPageNo(), findDictParam.getPageSize(), pageDict.getResults(), pageDict.getTotal());
    }
}
