package com.yuansaas.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * 地址辅助
 * @author LXZ 2025/12/15  12:18
 */
@Getter
@Setter
public class AddressModel {

    /**
     * 省区划编码
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市区划编码
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 区区划编码
     */
    private String districtCode;

    /**
     * 区名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String detailsAddress;
}
