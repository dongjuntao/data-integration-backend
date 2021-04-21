package com.softline.quartz.common;

import com.softline.common.util.ApplicationContextUtil;
import com.softline.common.util.RedisUtil;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * Created by dong ON 2021/1/29
 */
public class DistributedLockUtil {

    private static RedisUtil redisUtil
            = (RedisUtil) ApplicationContextUtil.getBean("redisUtil");

    /**
     * 尝试加锁
     * @param key
     * @param value
     * @return
     */
    public static boolean tryLock(String key, Object value, long time, TimeUnit timeUnit) {
        return redisUtil.setIfAbsent(key, value, time, timeUnit);
    }

    /**
     * 解锁
     * @param key
     */
    public static void releaseLock(String key) {
        redisUtil.del(key);
    }
}
