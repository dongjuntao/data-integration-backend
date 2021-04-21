package com.softline.security.config;

import com.softline.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 * Created by dong on 2020/11/24.
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
