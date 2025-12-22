package com.yuansaas.app.shop.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * 签约
 *
 * @author LXZ 2025/12/15 17:16
 */
@Data
public class SignedParam {
    /**
     * 商家id
     */
    @NotNull(message = "商家id不能为空")
    private Long id;
    /**
     * 签约人
     */
    @NotBlank(message = "签约人不能为空")
    private String name;
    /**
     * 签约时间
     */
    @NotNull(message = "签约时间不能为空")
    private LocalDateTime signeTime;
    /**
     * 到期时间
     */
    @NotNull(message = "到期时间不能为空")
    private LocalDateTime expireTime;
    /**
     * 合同编号
     */
    private String contractNo;

}
