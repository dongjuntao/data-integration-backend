package com.softline.controller;

import com.softline.common.api.CommonPage;
import com.softline.common.api.CommonResult;
import com.softline.mbg.model.DataCollectionScheduleJob;
import com.softline.quartz.enums.ScheduleJobStatus;
import com.softline.service.DataCollectionScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务管理
 * Created by dong ON 2020/12/15
 */
@RestController
@Api(tags = "DataCollectionScheduleController", description = "定时任务管理")
@RequestMapping("/dataCollectionSchedule")
public class DataCollectionScheduleController {

    @Autowired
    private DataCollectionScheduleService scheduleService;

    /**
     * 添加定时任务
     */
    @ApiOperation("添加定时任务")
    @PostMapping("/create")
    public CommonResult create(@RequestBody DataCollectionScheduleJob scheduleJob)  {
        scheduleJob.setStatus(ScheduleJobStatus.CREATED.name());//默认刚创建时位已创建状态
        return scheduleService.create(scheduleJob) > 0 ? CommonResult.success(scheduleJob) : CommonResult.failed();
    }

    /**
     * 修改定时任务
     */
    @ApiOperation("修改定时任务")
    @PutMapping("/update/{jobId}")
    public CommonResult update(@PathVariable Long jobId, @RequestBody DataCollectionScheduleJob scheduleJob)  {
        return scheduleService.update(jobId,scheduleJob) > 0 ? CommonResult.success(scheduleJob) : CommonResult.failed();
    }

    /**
     * 删除定时任务（支持单个和批量）
     */
    @ApiOperation("删除定时任务")
    @DeleteMapping("/delete/{jobIds}")
    public CommonResult delete(@PathVariable List<Long> jobIds)  {
        return scheduleService.delete(jobIds) > 0 ? CommonResult.success(scheduleService.delete(jobIds))
                : CommonResult.failed();
    }

    /**
     * 获取单个定时任务详情
     */
    @ApiOperation("获取单个定时任务详情")
    @GetMapping("/{id}")
    public CommonResult getDataSource(@PathVariable Long id)  {
        return scheduleService.getDataCollectionScheduleJob(id) != null ?
                CommonResult.success(scheduleService.getDataCollectionScheduleJob(id)) : CommonResult.failed();
    }

    /**
     * 分页获取定时任务列表
     */
    @ApiOperation("分页获取定时任务列表")
    @GetMapping("/listDataCollectionScheduleJob")
    public CommonResult<CommonPage<DataCollectionScheduleJob>> listDataCollectionScheduleJob(
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize)  {
        return CommonResult.success(CommonPage.restPage(scheduleService.listDataCollectionScheduleJob(keyword, pageSize, pageNum)));
    }

    /**
     * 修改定时任务生命周期状态
     */
    @ApiOperation("修改定时任务生命周期状态")
    @PutMapping("/updateStatus/{jobId}")
    public CommonResult updateStatus(@PathVariable Long jobId, @RequestParam(value = "status") String status)  {
        return scheduleService.updateStatus(jobId,status) > 0 ?
                CommonResult.success(scheduleService.updateStatus(jobId,status)) : CommonResult.failed();
    }

    /**
     * 立即执行任务
     */
    @ApiOperation("立即执行任务")
    @PostMapping("/run/{jobIds}")
    public CommonResult run(@PathVariable List<Long> jobIds){
        scheduleService.run(jobIds);
        return CommonResult.success("success");
    }

    /**
     * 暂停定时任务
     */
    @ApiOperation("暂停定时任务")
    @PostMapping("/pause/{jobIds}")
    public CommonResult pause(@PathVariable List<Long> jobIds){
        scheduleService.pause(jobIds);
        return CommonResult.success("success");
    }

    /**
     * 恢复定时任务
     */
    @ApiOperation("恢复定时任务")
    @PostMapping("/resume/{jobIds}")
    public CommonResult resume(@PathVariable List<Long> jobIds){
        scheduleService.resume(jobIds);
        return CommonResult.success("success");
    }
}
