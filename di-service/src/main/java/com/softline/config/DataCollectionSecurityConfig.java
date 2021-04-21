package com.softline.config;

import com.softline.service.SysAdminService;
import com.softline.security.component.DynamicSecurityService;
import com.softline.security.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Map;

/**
 * di-security模块相关配置
 * Created by dong on 2020/11/25.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DataCollectionSecurityConfig extends SecurityConfig {

    @Autowired
    private SysAdminService adminService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> adminService.loadUserByUsername(username);
    }

    /**需要访问权限控制时，可以加载需要限制的资源，暂时预留*/
    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                return null;
            }
        };
    }
}
