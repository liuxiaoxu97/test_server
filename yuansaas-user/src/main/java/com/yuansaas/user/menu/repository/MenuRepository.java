package com.yuansaas.user.menu.repository;

import com.yuansaas.user.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 *
 * @author LXZ 2025/10/21 11:47
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {


    Long countByMerchantCodeAndMenuCode(String merchantCode , String menuCode);

    List<Menu> findByMerchantCode(String merchantCode);

}
