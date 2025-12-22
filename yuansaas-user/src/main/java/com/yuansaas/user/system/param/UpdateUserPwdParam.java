package com.yuansaas.user.system.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * 修改用户密码参数
 *
 * @author LXZ 2025/12/21 17:51
 */
@Data
public class UpdateUserPwdParam {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;
    /**
     * 旧密码
     */
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     */
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
}
