package com.softline.service.impl;

import com.google.code.kaptcha.Producer;
import com.softline.common.exception.Asserts;
import com.softline.service.CaptchaCacheService;
import com.softline.service.CaptchaService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * 验证码处理类
 * Created by dong on 2020/11/25.
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private Producer producer;

    @Autowired
    private CaptchaCacheService captchaCacheService;


    /**
     * 生成验证码
     * @param uuid
     * @return
     */
    @Override
    public BufferedImage getCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            Asserts.fail("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();
        //验证码放到redis缓存
        captchaCacheService.setCaptcha(uuid, code);
        return producer.createImage(code);
    }

    /**
     * 验证码校验
     * @param uuid
     * @param code
     * @return
     */
    @Override
    public boolean validate(String uuid, String code) {
        String redisCode = captchaCacheService.getCaptcha(uuid);
        if (StringUtils.isBlank(redisCode)) {
            return false;
        }
        if(redisCode.equalsIgnoreCase(code)){
            //校验成功之后删除验证码
            captchaCacheService.deleteCaptcha(uuid);
            return true;
        }
        return false;
    }
}
