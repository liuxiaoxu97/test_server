package com.yuansaas.user.config;

import com.yuansaas.user.role.service.RoleMenuService;
import com.yuansaas.user.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 *  服务管理器 （解决依赖关系）
 * @author LXZ 2025/10/21 17:08
 */
@Component
public class ServiceManager {


    public static SysUserService sysUserService;
    public static RoleMenuService roleMenuService;
    @Autowired
    public void setSysUserService(SysUserService sysUserService) {
        ServiceManager.sysUserService = sysUserService;
    }
    @Autowired
    public void setRoleMenuService(RoleMenuService roleMenuService) {
        ServiceManager.roleMenuService = roleMenuService;
    }

}
