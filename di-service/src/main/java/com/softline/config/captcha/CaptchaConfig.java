package com.softline.config.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

/**
 * 验证码配置
 * Created by dong ON 2020/11/25
 */
@Configuration
public class CaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.image.width", "110");
        properties.setProperty("kaptcha.image.height", "38");
        properties.setProperty("kaptcha.textproducer.char.string","1234567890abcdefghijkmnopqrstuvwxyz");
        properties.setProperty("kaptcha.textproducer.font.size", "33");
        properties.setProperty("kaptcha.textproducer.char.space","3");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        // 这里 是去掉 噪点颜色
        properties.setProperty("kaptcha.noise.color", "255,96,0");
        // 样式自定义
        properties.setProperty("kaptcha.obscurificator.impl","com.softline.config.captcha.NoWaterRipple");
        // 配置使用原生的 无噪 实现类 NoNoise
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}

