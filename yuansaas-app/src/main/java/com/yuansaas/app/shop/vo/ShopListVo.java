package com.yuansaas.app.shop.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * 商家列表vo
 * @author LXZ 2025/12/12 10:44
 */
@Data
public class ShopListVo {
    /**
     * id
     */
    private Long id;
    /**
     * 商家名称
     */
    private String name;
    /**
     * 商家code
     */
    private String code;
    /**
     * 商家类型
     */
    private String type;
    /**
     * 签约状态
     */
    private String signedStatus;
    /**
     * 签约开始时间
     */
    private LocalDateTime signedStartAt;
    /**
     * 签约结束时间
     */
    private LocalDateTime signedEndAt;
    /**
     * 创建时间
     */
    private LocalDateTime createAt;
    /**
     * 锁定状态
     */
    private String lockStatus;


}
