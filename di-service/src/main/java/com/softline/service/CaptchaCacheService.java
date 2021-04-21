package com.softline.service;

/**
 * 验证码缓存接口
 * Created by dong ON 2020/12/2
 */
public interface CaptchaCacheService {

    void setCaptcha(String uuid, String code);

    String getCaptcha(String uuid);

    void deleteCaptcha(String uuid);
}
