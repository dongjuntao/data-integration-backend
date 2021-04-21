package com.softline.service.impl;

import com.softline.common.util.RedisUtil;
import com.softline.mbg.model.SysAdminUser;
import com.softline.service.SysAdminCacheService;
import com.softline.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SysAdminCacheServiceImpl实现类
 * Created by dong on 2020/11/26.
 */
@Service
public class SysAdminCacheServiceImpl implements SysAdminCacheService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysAdminService adminService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Override
    public void deleteAdmin(Long adminId) {
        SysAdminUser sysAdminUser = adminService.getItem(adminId);
        if (sysAdminUser != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + sysAdminUser.getUsername();
            redisUtil.del(key);
        }
    }

    @Override
    public SysAdminUser getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (SysAdminUser) redisUtil.get(key);
    }

    @Override
    public void setAdmin(SysAdminUser sysAdminUser) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + sysAdminUser.getUsername();
        redisUtil.set(key, sysAdminUser, REDIS_EXPIRE);
    }

}
