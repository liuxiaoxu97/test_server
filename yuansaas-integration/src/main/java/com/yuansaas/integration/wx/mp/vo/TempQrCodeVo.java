package com.yuansaas.integration.wx.mp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 临时二维码获取
 *
 * @author HTB 2025/8/26 18:23
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TempQrCodeVo {

    /**
     * 二维码地址
     */
    private String url;

    /**
     * 场景值ID
     */
    private String sceneId;
}
