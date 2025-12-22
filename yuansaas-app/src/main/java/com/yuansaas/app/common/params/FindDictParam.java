package com.yuansaas.app.common.params;

import com.yuansaas.app.common.enums.PlatformTypeEnum;
import com.yuansaas.core.page.PageModel;
import lombok.Data;

/**
 *
 * 查询字典类型
 *
 * @author LXZ 2025/11/19 18:38
 */
@Data
public class FindDictParam  extends PageModel {
    /**
     * 字典类型名称
     */
    private String dictName;
    /**
     * 平台类型
     */
    private PlatformTypeEnum platform;
}
