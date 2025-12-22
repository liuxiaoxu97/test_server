package com.yuansaas.app.shop.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuansaas.app.shop.entity.QShop;
import com.yuansaas.app.shop.entity.Shop;
import com.yuansaas.app.shop.enums.ShopTypeEnum;
import com.yuansaas.app.shop.param.FindShopParam;
import com.yuansaas.app.shop.param.SaveShopParam;
import com.yuansaas.app.shop.param.SignedParam;
import com.yuansaas.app.shop.param.UpdateShopParam;
import com.yuansaas.app.shop.repository.ShopRepository;
import com.yuansaas.app.shop.service.ShopService;
import com.yuansaas.app.shop.service.mapstruct.ShopMapStruct;
import com.yuansaas.app.shop.vo.ShopListVo;
import com.yuansaas.common.constants.AppConstants;
import com.yuansaas.core.context.AppContextUtil;
import com.yuansaas.core.exception.ex.DataErrorCode;
import com.yuansaas.core.jpa.querydsl.BoolBuilder;
import com.yuansaas.core.page.RPage;
import com.yuansaas.user.dept.params.SaveDeptParam;
import com.yuansaas.user.dept.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 *
 * 商家实现类
 *
 * @author LXZ 2025/12/12 10:55
 */
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopMapStruct shopMapStruct;
    private final ShopRepository shopRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final DeptService deptService;

    /**
     * 添加商家
     *
     * @param saveShopParam 商家参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean add(SaveShopParam saveShopParam) {
        Shop saveShop = shopMapStruct.toSaveShop(saveShopParam);
        // 生成code
        saveShop.setCode(getCode(saveShopParam.getType()));
        shopRepository.save(saveShop);
        // 生成默认部门id
        SaveDeptParam saveDeptParam = new SaveDeptParam();
        saveDeptParam.setName(saveShop.getName());
        saveDeptParam.setPid(-1l);
        saveDeptParam.setMerchantCode(saveShop.getCode());
        deptService.save(saveDeptParam);
        return true;
    }

    /**
     * 编辑商家
     *
     * @param updateShopParam 商家参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean update(UpdateShopParam updateShopParam) {
        Shop shop = shopRepository.findById(updateShopParam.getId()).orElse(null);
        if (ObjectUtils.isEmpty(shop)) {
            throw DataErrorCode.DATA_ALREADY_EXISTS.buildException("商家不存在");
        }
        shopMapStruct.toUpdateShop(shop,updateShopParam);
        shopRepository.save(shop);
        return true;
    }

    /**
     * 禁用商家
     *
     * @param id 商家id
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean lock(Long id) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (ObjectUtils.isEmpty(shop)) {
            throw DataErrorCode.DATA_ALREADY_EXISTS.buildException("商家不存在");
        }
        shop.setLockStatus(AppConstants.N.equals(shop.getLockStatus()) ? AppConstants.Y : AppConstants.N);
        shop.setUpdateAt(LocalDateTime.now());
        shop.setUpdateBy(AppContextUtil.getUserInfo());
        shopRepository.save(shop);
        return true;
    }

    /**
     * 删除商家
     *
     * @param id 商家id
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean delete(Long id) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (ObjectUtils.isEmpty(shop)) {
            throw DataErrorCode.DATA_ALREADY_EXISTS.buildException("商家不存在");
        }
        shop.setDeleteStatus(AppConstants.Y);
        shopRepository.save(shop);
        return true;
    }

    /**
     * 查询商家列表
     *
     * @param findShopParam 查询参数
     * @return 商家列表
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public RPage<ShopListVo> getByPage(FindShopParam findShopParam) {
        QShop qShop = QShop.shop;
        QueryResults<ShopListVo> longQueryResults = jpaQueryFactory.select(Projections.bean(
                        ShopListVo.class,
                        qShop.id,
                        qShop.name,
                        qShop.code,
                        qShop.type,
                        qShop.signedStatus,
                        qShop.signedStartAt,
                        qShop.signedEndAt,
                        qShop.createAt,
                        qShop.lockStatus
                ))
                .from(qShop)
                .where(BoolBuilder.getInstance()
                        .and(findShopParam.getCode(), qShop.code::eq)
                        .and(findShopParam.getName(), qShop.name::contains)
                        .and(findShopParam.getSignedStatus(), qShop.signedStatus::eq)
                        .and(AppConstants.N , qShop.deleteStatus::eq)
                        .getWhere())
                .orderBy(qShop.createAt.desc())
                .limit(findShopParam.getPageSize())
                .offset(findShopParam.obtainOffset())
                .fetchResults();

        if (ObjectUtils.isEmpty(longQueryResults)) {
            return new RPage<>(findShopParam.getPageNo(), findShopParam.getPageSize());
        }
        return new RPage<>(findShopParam.getPageNo(), findShopParam.getPageSize(),longQueryResults.getResults(),longQueryResults.getTotal());
    }

    /**
     * 签约操作
     *
     * @param signedParam 签约参数
     * @author lxz 2025/11/16 14:35
     */
    @Override
    public Boolean signed(SignedParam signedParam) {
        Shop shop = shopRepository.findById(signedParam.getId()).orElse(null);
        if (ObjectUtils.isEmpty(shop)) {
            throw DataErrorCode.DATA_NOT_FOUND.buildException();
        }
        shop.setSignedStatus(AppConstants.Y);
        shop.setSignedUserId(0L);
        shop.setSignedUserName(signedParam.getName());
        shop.setSignedStartAt(signedParam.getSigneTime());
        shop.setSignedEndAt(signedParam.getExpireTime());
        shop.setUpdateAt(LocalDateTime.now());
        shop.setUpdateBy(AppContextUtil.getUserInfo());
        shopRepository.save(shop);
        return true;
    }


    /**
     * 生成 商家code
     */
    private String getCode(ShopTypeEnum shopType) {
        // 生成code
        String code =  shopType.name().concat("_").concat(RandomUtil.randomStringUpper(4));
        // 判断code是否存在
        Integer count = shopRepository.countByCode(code);
        if (count > 0) {
            getCode(shopType);
        }
        return code;

    }

}
