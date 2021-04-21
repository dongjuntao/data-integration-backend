package com.softline.service.impl;

import com.softline.common.util.RedisUtil;
import com.softline.service.CaptchaCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 验证码缓存实现类
 * Created by dong ON 2020/12/2
 */
@Service
public class CaptchaCacheServiceImpl implements CaptchaCacheService {

    @Autowired
    private RedisUtil redisUtil;
    /**验证码存放redis库*/
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    /**验证码有效期*/
    @Value("${redis.expire.captcha}")
    private Long REDIS_EXPIRE_CAPTCHA;
    /**验证码redis key*/
    @Value("${redis.key.captcha}")
    private String REDIS_KEY_CAPTCHA;

    @Override
    public void setCaptcha(String uuid, String code) {
        String key = REDIS_DATABASE + ":" +REDIS_KEY_CAPTCHA + ":" + uuid;
        redisUtil.set(key, code, REDIS_EXPIRE_CAPTCHA);
    }

    @Override
    public String getCaptcha(String uuid) {
        String key = REDIS_DATABASE + ":" +REDIS_KEY_CAPTCHA + ":" + uuid;
        return String.valueOf(redisUtil.get(key));
    }

    @Override
    public void deleteCaptcha(String uuid) {
        String key = REDIS_DATABASE + ":" +REDIS_KEY_CAPTCHA + ":" + uuid;
        redisUtil.del(key);
    }
}
