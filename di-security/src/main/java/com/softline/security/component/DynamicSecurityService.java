package com.softline.security.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * 动态权限相关业务类
 * Created by dong on 2020/11/24.
 */
public interface DynamicSecurityService {
    /**
     * 需要访问权限控制时，可以加载需要限制的资源，暂时预留
     */
    Map<String, ConfigAttribute> loadDataSource();
}
