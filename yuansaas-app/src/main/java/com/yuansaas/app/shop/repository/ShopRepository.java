package com.yuansaas.app.shop.repository;

import com.yuansaas.app.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 *
 * @author LXZ 2025/12/12 15:28
 */
public interface ShopRepository extends JpaRepository<Shop,Long> {


    Integer countByCode(String code);
}
