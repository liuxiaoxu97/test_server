package com.yuansaas.app.user.client.param;

import com.yuansaas.user.common.enums.UserType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 测试model
 *
 * @author HTB 2025/7/22 15:46
 */
@Data
public class TestModel {

    /**
     *  id
     */
    private Long id;
    /**
     *  名称
     */
    private String name;
    /**
     *  编码
     */
    private String code;
    /**
     *  备注
     */
    private String remark;
    /**
     *  用户类型
     */
    private UserType userType;
    /**
     *  创建时间
     */
    private LocalDateTime localDateTime;
    /**
     *  创建日期
     */
    private LocalDate localDate;
    /**
     *  创建时间
     */
    private LocalTime localTime;

}
