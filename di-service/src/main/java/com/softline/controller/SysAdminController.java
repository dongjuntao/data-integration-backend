package com.softline.controller;

import cn.hutool.core.collection.CollUtil;
import com.softline.common.api.CommonPage;
import com.softline.common.api.CommonResult;
import com.softline.dto.SysAdminLoginParam;
import com.softline.dto.SysAdminParam;
import com.softline.dto.UpdateAdminPasswordParam;
import com.softline.mbg.model.SysAdminUser;
import com.softline.mbg.model.SysRole;
import com.softline.service.SysAdminService;
import com.softline.service.CaptchaService;
import com.softline.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台用户管理
 * Created by dong on 2020/11/25.
 */
@RestController
@Api(tags = "SysAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class SysAdminController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private SysAdminService adminService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private CaptchaService sysCaptchaService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public CommonResult<SysAdminUser> register(@Validated @RequestBody SysAdminParam sysAdminParam) {
        SysAdminUser sysAdminUser = adminService.register(sysAdminParam);
        if (sysAdminUser == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(sysAdminUser);
    }

    @ApiOperation(value = "登录以后返回token")
    @PostMapping("/login")
    public CommonResult login(@Validated @RequestBody SysAdminLoginParam sysAdminLoginParam) {
        boolean validate = sysCaptchaService.validate(sysAdminLoginParam.getUuid(), sysAdminLoginParam.getCaptcha());
        if (!validate) {
            return CommonResult.validateFailed("验证码错误或已过期");
        }
        String token = adminService.login(sysAdminLoginParam.getUsername(), sysAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "刷新token")
    @GetMapping("/refreshToken")
    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/info")
    public CommonResult getAdminInfo(Principal principal) {
        if(principal==null){
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        SysAdminUser sysAdminUser = adminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", sysAdminUser.getUsername());
        data.put("menus", roleService.getMenuList(sysAdminUser.getId()));
        List<SysRole> roleList = adminService.getRoleList(sysAdminUser.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(SysRole::getName).collect(Collectors.toList());
            data.put("roles",roles);
        }
        return CommonResult.success(data);
    }

    @ApiOperation(value = "登出功能")
    @PostMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<SysAdminUser>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SysAdminUser> adminList = adminService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @ApiOperation("获取指定用户信息")
    @GetMapping("/{id}")
    public CommonResult<SysAdminUser> getItem(@PathVariable Long id) {
        SysAdminUser admin = adminService.getItem(id);
        return CommonResult.success(admin);
    }

    @ApiOperation("修改指定用户信息")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SysAdminUser sysAdminUser) {
        int count = adminService.update(id, sysAdminUser);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定用户密码")
    @PostMapping("/updatePassword")
    public CommonResult updatePassword(@Validated @RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该用户");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除指定用户信息")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = adminService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改帐号状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id,@RequestParam(value = "status") Integer status) {
        SysAdminUser sysAdminUser = new SysAdminUser();
        sysAdminUser.setStatus(status);
        int count = adminService.update(id,sysAdminUser);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("给用户分配角色")
    @PostMapping("/role/update")
    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @GetMapping("/role/{adminId}")
    public CommonResult<List<SysRole>> getRoleList(@PathVariable Long adminId) {
        List<SysRole> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }

}
