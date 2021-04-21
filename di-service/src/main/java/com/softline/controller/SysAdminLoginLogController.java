package com.softline.controller;

import com.softline.common.api.CommonPage;
import com.softline.common.api.CommonResult;
import com.softline.mbg.model.SysAdminLoginLog;
import com.softline.service.SysAdminLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理员登录日志
 * Created by dong ON 2020/11/25
 */
@RestController
@Api(tags = "SysAdminLoginLogController", description = "后台管理员登录日志")
@RequestMapping("/admin")
public class SysAdminLoginLogController {

    @Autowired
    private SysAdminLoginLogService sysAdminLoginLogService;

    /**
     * 分页获取管理员用户登录日志列表
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
    @ApiOperation(value = "管理员登录日志列表")
    @GetMapping("/log")
    public CommonResult<CommonPage<SysAdminLoginLog>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SysAdminLoginLog> dataList = sysAdminLoginLogService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(dataList));
    }
}
