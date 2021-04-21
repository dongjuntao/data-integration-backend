package com.softline.controller;

/**
 * Created by dong ON 2020/11/26
 */
import com.softline.common.api.CommonResult;
import com.softline.database.common.DatabaseParam;
import com.softline.service.MysqlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * mysql相关
 * Created by dong on 2020/11/25.
 */
@RestController
@Api(tags = "MysqlController", description = "管理")
@RequestMapping("/mysql")
public class MysqlController {

    @Autowired
    private MysqlService mysqlService;

    /**
     * @param databaseParam
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据数据库相关参数获取库下所有的表结构信息")
    @PostMapping("/getTableField")
    public CommonResult getTableField(@RequestBody DatabaseParam databaseParam) throws Exception {
        return CommonResult.success(mysqlService.getTableNamesAndFieldsByDataByDBName(databaseParam));
    }

    /**
     * @param databaseParam
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据数据库相关参数获取库下所有的表")
    @PostMapping("/getTableList")
    public CommonResult getTableList(@RequestBody DatabaseParam databaseParam) throws SQLException {
        return CommonResult.success(mysqlService.getTableNameList(databaseParam));
    }


    /**
     * @param databaseParam
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据表名获取表结构信息")
    @PostMapping("/getFieldList/{tableName}")
    public CommonResult getFieldsByTableName(@PathVariable String tableName,
                                             @RequestBody DatabaseParam databaseParam) throws SQLException {
        return CommonResult.success(mysqlService.getFieldsByTableName(tableName,databaseParam));
    }


    /**
     * @param databaseParam
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "测试数据库是否连通")
    @PostMapping("/test")
    public CommonResult get(@RequestBody DatabaseParam databaseParam) throws Exception {
        return CommonResult.success(mysqlService.isConnected(databaseParam));
    }

}
