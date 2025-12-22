package com.yuansaas.user.auth.model;

import com.yuansaas.common.enums.IBaseEnum;
import com.yuansaas.user.client.entity.ClientUser;
import com.yuansaas.user.common.enums.UserStatus;
import com.yuansaas.user.common.enums.UserType;
import com.yuansaas.user.system.entity.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 自定义用户详情
 *
 * @author HTB 2025/8/5 15:40
 */
@Data
public class CustomUserDetails implements UserDetails {

    /**
     * 用户ID
     */
    private final Long userId;

    /**
     * 用户类型
     */
    private final UserType userType;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 头像
     */
    private final String avatar;

    /**
     * 密码
     */
    private final String password;

    /**
     * 账号是否可用
     */
    private final boolean enabled;

    /**
     * 是否锁定
     */
    private final boolean locked;

    /**
     * 用户状态
     */
    private UserStatus status;

    /**
     * 权限列表
     */
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(ClientUser clientUser){
         this.userId = clientUser.getId();
         this.userType = UserType.CLIENT_USER;
         this.username = clientUser.getUserName();
         this.avatar = clientUser.getAvatarUrl();
         this.password = clientUser.getPasswordHash() ;
         this.enabled = UserStatus.active.matches(clientUser.getStatus());
         this.locked = UserStatus.active.matches(clientUser.getStatus());
         IBaseEnum.fromNameIgnoreCase(clientUser.getStatus() , UserStatus.class).ifPresent(userStatus -> this.status = userStatus);
         this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_".concat(userType.getName())));
    }

    public CustomUserDetails(SysUser sysUser){
        this.userId = sysUser.getId();
        this.userType = UserType.SYSTEM_USER;
        this.username = sysUser.getUserName();
        this.avatar = null;
        this.enabled = UserStatus.active.matches(sysUser.getStatus());
        this.password = sysUser.getPassword() ;
        this.locked = UserStatus.active.matches(sysUser.getStatus());
        IBaseEnum.fromNameIgnoreCase(sysUser.getStatus() , UserStatus.class).ifPresent(userStatus -> this.status = userStatus);
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_".concat(userType.getName())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
