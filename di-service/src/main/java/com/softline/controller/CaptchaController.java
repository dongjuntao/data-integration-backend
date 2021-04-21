package com.softline.controller;

import com.softline.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码相关
 * Created by dong ON 2020/11/25
 */
@RestController
@Api(tags = "SysCaptchaController", description = "验证码")
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService sysCaptchaService;

    /**
     * 获取验证码
     */
    @ApiOperation("获取验证码")
    @GetMapping("/generate")
    public void getCaptcha(HttpServletResponse response, HttpServletRequest request, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }
}
