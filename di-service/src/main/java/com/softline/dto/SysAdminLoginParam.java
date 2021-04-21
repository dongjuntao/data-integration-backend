package com.softline.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登录参数
 * Created by dong on 2020/11/26.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysAdminLoginParam {
    @NotEmpty
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @NotEmpty
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @NotEmpty
    @ApiModelProperty(value = "验证码", required = true)
    private String captcha;
    @NotEmpty
    @ApiModelProperty(value = "验证码因子uuid", required = true)
    private String uuid;
}
