package com.softline.service;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

public interface CaptchaService {
    /**
     * 获取验证码
     */
    BufferedImage getCaptcha(String uuid);

    boolean validate(String uuid, String code);
}
