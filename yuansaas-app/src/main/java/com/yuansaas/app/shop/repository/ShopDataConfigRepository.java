package com.yuansaas.app.shop.repository;

import com.yuansaas.app.shop.entity.Shop;
import com.yuansaas.app.shop.entity.ShopDataConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 *
 * @author LXZ 2025/12/12 15:28
 */
public interface ShopDataConfigRepository extends JpaRepository<ShopDataConfig,Long> {


    Integer countByShopCode(String code);

    ShopDataConfig findByShopCode(String code);
}
