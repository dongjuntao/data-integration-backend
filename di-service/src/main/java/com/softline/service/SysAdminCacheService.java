package com.softline.service;

import com.softline.mbg.model.SysAdminUser;

/**
 * 后台用户缓存操作类
 * Created by dong on 2020/11/26.
 */
public interface SysAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void deleteAdmin(Long adminId);

    /**
     * 获取缓存后台用户信息
     */
    SysAdminUser getAdmin(String username);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(SysAdminUser admin);

}
