package com.yuansaas.user.system.param;

import com.yuansaas.core.page.PageModel;
import com.yuansaas.user.common.enums.UserStatus;
import lombok.Data;

/**
 *
 * 查询用户信息
 *
 * @author LXZ 2025/10/29 10:16
 */
@Data
public class FindUserParam extends PageModel {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 状态
     */
    private UserStatus status;

}
