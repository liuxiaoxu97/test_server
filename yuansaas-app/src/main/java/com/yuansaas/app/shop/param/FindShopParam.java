package com.yuansaas.app.shop.param;

import com.yuansaas.core.page.PageModel;
import lombok.Data;

/**
 *
 * 查询商家列表
 *
 * @author LXZ 2025/12/12 10:40
 */
@Data
public class FindShopParam extends PageModel {

    /**
     * 商家名称
     */
    private String name;
    /**
     * 商家编码
     */
    private String code;
    /**
     * 签约类型
     */
    private String signedStatus;
}
