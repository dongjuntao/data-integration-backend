package com.softline.bo;

import com.softline.mbg.model.SysAdminUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * SpringSecurity需要的用户详情
 * Created by dong on 2020/11/25.
 */
public class AdminUserDetails implements UserDetails {
    private SysAdminUser sysAdminUser;
    public AdminUserDetails(SysAdminUser sysAdminUser) {
        this.sysAdminUser = sysAdminUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的角色
        //return resourceList.stream()
                //.map(role ->new SimpleGrantedAuthority(role.getId()+":"+role.getName()))
                //.collect(Collectors.toList());
        return null;
    }

    @Override
    public String getPassword() {
        return sysAdminUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysAdminUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return sysAdminUser.getStatus().equals(1);
    }
}
