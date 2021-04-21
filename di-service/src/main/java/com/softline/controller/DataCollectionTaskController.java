package com.softline.controller;

import com.softline.common.api.CommonPage;
import com.softline.common.api.CommonResult;
import com.softline.enums.DataCollectionTaskStatusEnum;
import com.softline.mbg.model.DataCollectionTask;
import com.softline.service.DataCollectionTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据采集任务controller（关系型数据库）
 * Created by dong ON 2020/12/5
 */
@RestController
@Api(tags = "DataCollectionTaskController", description = "数据源管理")
@RequestMapping("/dataCollectionTask")
public class DataCollectionTaskController {

    @Autowired
    private DataCollectionTaskService dataCollectionTaskService;

    /**
     * 添加数据采集任务
     */
    @ApiOperation("添加数据采集任务")
    @PostMapping("/create")
    public CommonResult create(@RequestBody DataCollectionTask dataCollectionTask)  {
        //刚创建是默认为"已创建状态"
        dataCollectionTask.setStatus(DataCollectionTaskStatusEnum.CREATED.name());
        return dataCollectionTaskService.create(dataCollectionTask) > 0 ? CommonResult.success(dataCollectionTask) : CommonResult.failed();
    }

    /**
     * 修改数据源
     */
    @ApiOperation("修改数据采集任务")
    @PutMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody DataCollectionTask dataCollectionTask)  {
        return dataCollectionTaskService.update(id,dataCollectionTask) > 0 ? CommonResult.success(dataCollectionTask) : CommonResult.failed();
    }

    /**
     * 删除数据采集任务（支持单个和批量）
     */
    @ApiOperation("删除数据采集任务")
    @DeleteMapping("/delete/{ids}")
    public CommonResult delete(@PathVariable List<Long> ids)  {
        return dataCollectionTaskService.delete(ids) > 0 ? CommonResult.success(dataCollectionTaskService.delete(ids))
                : CommonResult.failed();
    }

    /**
     * 获取单个数据源采集任务详情
     */
    @ApiOperation("获取单个数据源采集任务详情")
    @GetMapping("/{id}")
    public CommonResult getDataCollectionTask(@PathVariable Long id)  {
        return dataCollectionTaskService.getDataCollectionTask(id) != null ?
                CommonResult.success(dataCollectionTaskService.getDataCollectionTask(id)) : CommonResult.failed();
    }

    /**
     * 分页获取数据采集任务列表
     */
    @ApiOperation("分页获取数据采集任务列表")
    @GetMapping("/listDataCollectionTask")
    public CommonResult<CommonPage<DataCollectionTask>> listDataSources(
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize)  {
        return CommonResult.success(CommonPage.restPage(dataCollectionTaskService.
                listDataCollectionTask(keyword, pageSize, pageNum)));
    }

    /**
     * 获取所有数据源列表（不分页）
     */
    @ApiOperation("获取所有数据源列表")
    @GetMapping("/listAllDataCollectionTask")
    public CommonResult<List<DataCollectionTask>> listAllDataCollectionTask (
            @RequestParam(value = "status",required = false) String status)  {
        return CommonResult.success(dataCollectionTaskService.listAllDataCollectionTask(status));
    }

    /**
     * 修改数据源生命周期状态
     */
    @ApiOperation("修改数据源生命周期状态")
    @PutMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") String status)  {
        return dataCollectionTaskService.updateStatus(id,status) > 0 ?
                CommonResult.success(dataCollectionTaskService.updateStatus(id,status)) : CommonResult.failed();
    }

}
