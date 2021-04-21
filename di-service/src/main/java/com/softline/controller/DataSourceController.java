package com.softline.controller;

import com.softline.common.api.CommonPage;
import com.softline.common.api.CommonResult;
import com.softline.enums.DataSourceStatusEnum;
import com.softline.mbg.model.DataCollectionDataSource;
import com.softline.service.DataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源管理
 * Created by dong ON 2020/12/2
 */
@RestController
@Api(tags = "DataSourceController", description = "数据源管理")
@RequestMapping("/dataSource")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 添加数据源
     */
    @ApiOperation("添加数据源")
    @PostMapping("/create")
    public CommonResult create(@RequestBody DataCollectionDataSource dataSource)  {
        dataSource.setStatus(DataSourceStatusEnum.CREATED.name());//默认刚创建是为“已创建”状态
        return dataSourceService.create(dataSource) > 0 ? CommonResult.success(dataSource) : CommonResult.failed();
    }

    /**
     * 修改数据源
     */
    @ApiOperation("修改数据源")
    @PutMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody DataCollectionDataSource dataSource)  {
        return dataSourceService.update(id,dataSource) > 0 ? CommonResult.success(dataSource) : CommonResult.failed();
    }

    /**
     * 删除数据源（支持单个和批量）
     */
    @ApiOperation("删除数据源")
    @DeleteMapping("/delete/{ids}")
    public CommonResult delete(@PathVariable List<Long> ids)  {
        return dataSourceService.delete(ids) > 0 ? CommonResult.success(dataSourceService.delete(ids))
                : CommonResult.failed();
    }

    /**
     * 获取单个数据源详情
     */
    @ApiOperation("获取单个数据源详情")
    @GetMapping("/{id}")
    public CommonResult getDataSource(@PathVariable Long id)  {
        return dataSourceService.getDataSource(id) != null ?
                CommonResult.success(dataSourceService.getDataSource(id)) : CommonResult.failed();
    }

    /**
     * 分页获取数据源列表
     */
    @ApiOperation("分页获取数据源列表")
    @GetMapping("/listDataSource")
    public CommonResult<CommonPage<DataCollectionDataSource>> listDataSources(
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize)  {
        return CommonResult.success(CommonPage.restPage(dataSourceService.listDataSource(keyword, pageSize, pageNum)));
    }

    /**
     * 获取所有数据源列表（不分页）
     */
    @ApiOperation("获取所有数据源列表")
    @GetMapping("/listAllDataSource")
    public CommonResult<List<DataCollectionDataSource>> listAllDataSource (
            @RequestParam(value = "status",required = false) String status)  {
        return CommonResult.success(dataSourceService.listAllDataSource(status));
    }


    /**
     * 修改数据源生命周期状态
     */
    @ApiOperation("修改数据源生命周期状态")
    @PutMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") String status)  {
        return dataSourceService.updateStatus(id,status) > 0 ?
                CommonResult.success(dataSourceService.updateStatus(id,status)) : CommonResult.failed();
    }
}
