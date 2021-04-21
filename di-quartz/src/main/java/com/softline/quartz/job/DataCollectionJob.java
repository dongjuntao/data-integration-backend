package com.softline.quartz.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.softline.common.util.ApplicationContextUtil;
import com.softline.database.common.DatabaseParam;
import com.softline.database.common.FieldTypeConvert;
import com.softline.database.mysql.DBUtil;
import com.softline.mbg.mapper.DataCollectionDataSourceMapper;
import com.softline.mbg.mapper.DataCollectionScheduleJobMapper;
import com.softline.mbg.mapper.DataCollectionTaskMapper;
import com.softline.mbg.model.DataCollectionDataSource;
import com.softline.mbg.model.DataCollectionScheduleJob;
import com.softline.mbg.model.DataCollectionTask;
import com.softline.database.enums.DatabaseType;
import com.softline.quartz.common.CronUtil;
import com.softline.quartz.config.Constant;
import com.softline.quartz.enums.ScheduleJobStatus;
import com.softline.quartz.common.DistributedLockUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 关系型数据库采集定时任务
 * Created by dong ON 2020/12/15
 */
@DisallowConcurrentExecution
public class DataCollectionJob extends QuartzJobBean {

    public DataCollectionJob() {
        super();
    }

    private DataCollectionTaskMapper dataCollectionTaskMapper
            = (DataCollectionTaskMapper) ApplicationContextUtil.getBean("dataCollectionTaskMapper");

    private DataCollectionDataSourceMapper dataSourceMapper
            = (DataCollectionDataSourceMapper) ApplicationContextUtil.getBean("dataCollectionDataSourceMapper");

    private DataCollectionScheduleJobMapper scheduleJobMapper
            = (DataCollectionScheduleJobMapper) ApplicationContextUtil.getBean("dataCollectionScheduleJobMapper");
    /***
     * 任务执行体
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long start = System.currentTimeMillis();
        String jobKeyName = "";
        DataCollectionScheduleJob scheduleJob = null;
        try {
            jobKeyName = jobExecutionContext.getJobDetail().getKey().getName();
            //获取定时任务参数
            scheduleJob = (DataCollectionScheduleJob)jobExecutionContext
                    .getJobDetail().getJobDataMap().get(jobKeyName);
            String cron = scheduleJob.getCronExpression();
            //该失效时间为分布式锁失效时间，避免异常后锁无法清除的问题，为定时任务周期加上5分钟
            long expiredTime = CronUtil.getPeriodByCron(cron);
            boolean isLocked = DistributedLockUtil.tryLock(Constant.DB_COLLECTION_REDIS_KEY_PREFIX+":"+jobKeyName,
                    jobKeyName+"("+expiredTime+"ms)", expiredTime+(1000*60*5), TimeUnit.MILLISECONDS);
            //分布式锁，避免多任务时抢占任务
            if (!isLocked) {
                return;
            }

            //获取采集任务id
            Long dataCollectionTaskId = scheduleJob.getDataCollectionTaskId();
            DataCollectionTask dataCollectionTask = dataCollectionTaskMapper.selectByPrimaryKey(dataCollectionTaskId);
            if (dataCollectionTask == null) {
                return;
            }
            //获取已选择的表和字段相关信息
            JSONArray selectedTablesFieldsJSONArray = JSONObject.parseArray(dataCollectionTask.getSelectedTablesFields());
            //输出数据源和输出数据源相关信息
            DataCollectionDataSource exportDataSource = dataSourceMapper
                    .selectByPrimaryKey(dataCollectionTask.getExportDataSourceId());
            DataCollectionDataSource importDataSource = dataSourceMapper
                    .selectByPrimaryKey(dataCollectionTask.getImportDataSourceId());
            //创建表
            handleExportDataSource(getDataBaseParam(importDataSource),getDataBaseParam(exportDataSource), selectedTablesFieldsJSONArray, scheduleJob);
            //保存数据
            handleBatchInsertData(getDataBaseParam(importDataSource),getDataBaseParam(exportDataSource),
                    selectedTablesFieldsJSONArray, scheduleJob);
            //保存定时任务状态
            scheduleJob.setStatus(ScheduleJobStatus.SUCCESS.name());
            scheduleJobMapper.updateByPrimaryKey(scheduleJob);
            //释放锁
            DistributedLockUtil.releaseLock(Constant.DB_COLLECTION_REDIS_KEY_PREFIX+":"+jobKeyName);
        }catch (Exception e) {
            scheduleJob.setStatus(ScheduleJobStatus.FAIL.name());
            scheduleJobMapper.updateByPrimaryKey(scheduleJob);
        }finally {
            System.out.println("time： " + (System.currentTimeMillis() - start));
        }
    }

    /**
     * 创建表结构
     * @param importParam
     * @param exportParam
     * @param selectedTablesFieldsJSONArray
     * @param scheduleJob
     */
    public void handleExportDataSource(DatabaseParam importParam, DatabaseParam exportParam,
                                       JSONArray selectedTablesFieldsJSONArray, DataCollectionScheduleJob scheduleJob) {
        try {
            String importDatabaseType = "";
            if (importParam.getDriverClassName().indexOf(DatabaseType.mysql.name()) != -1) {
                importDatabaseType = DatabaseType.mysql.name();
            }else if (importParam.getDriverClassName().indexOf(DatabaseType.oracle.name()) != -1) {
                importDatabaseType = DatabaseType.oracle.name();
            }else if (importParam.getDriverClassName().indexOf(DatabaseType.sqlserver.name()) != -1) {
                importDatabaseType = DatabaseType.sqlserver.name();
            }
            //为mysql数据库创建表
            if (exportParam.getDriverClassName().indexOf(DatabaseType.mysql.name()) != -1) {
                createTableForMysql(importDatabaseType, exportParam, selectedTablesFieldsJSONArray);
            }
            //为oracle数据库创建表
            else if (exportParam.getDriverClassName().indexOf(DatabaseType.oracle.name()) != -1) {
                createTableForOracle(importDatabaseType, exportParam, selectedTablesFieldsJSONArray);
            }
            //为sqlServer数据库创建表
            else if (exportParam.getDriverClassName().indexOf(DatabaseType.sqlserver.name()) != -1) {
                createTableForSqlServer(importDatabaseType, exportParam, selectedTablesFieldsJSONArray);
            }
        }catch (Exception e) {
            scheduleJob.setStatus(ScheduleJobStatus.FAIL.name());
            scheduleJobMapper.updateByPrimaryKey(scheduleJob);
        }
    }

    /**
     * mysql循环创建表
     * @param importDatabaseType
     * @param exportParam
     * @param selectedTablesFieldsJSONArray
     */
    public void createTableForMysql (String importDatabaseType, DatabaseParam exportParam, JSONArray selectedTablesFieldsJSONArray) {
        for (int i=0,iLen=selectedTablesFieldsJSONArray.size(); i<iLen; i++) {
            JSONObject obj = (JSONObject)selectedTablesFieldsJSONArray.get(i);
            StringBuffer sqlBuffer = new StringBuffer();
            DBUtil.dropTable(exportParam,  obj.getString("tableName")); //先删除表
            sqlBuffer.append("CREATE TABLE " +  obj.getString("tableName") + "(");
            JSONArray fieldArray = obj.getJSONArray("fieldList"); //字段信息
            for (int j=0,jLen=fieldArray.size(); j<jLen; j++) {
                JSONObject tableInfo = fieldArray.getJSONObject(j);
                sqlBuffer.append(tableInfo.getString("columnName") + " ");
                String columnType = tableInfo.getString("columnType");
                int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
                int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
                String columnTypeAndSize = "";
                //如果是MySQL本身，获取字段类型，需要指定size再指定size
                if (DatabaseType.mysql.name().equalsIgnoreCase(importDatabaseType)) {
                    columnTypeAndSize = FieldTypeConvert.getFieldTypeAndSize(DatabaseType.mysql.name(), columnType, columnSize, decimalDigits);
                }//如果输入为oracle，需要做类型转换
                else if (DatabaseType.oracle.name().equalsIgnoreCase(importDatabaseType)) {
                    columnTypeAndSize = FieldTypeConvert.getConvertedFieldType(DatabaseType.oracle.name(), DatabaseType.mysql.name(), tableInfo);
                }//如果输入为sqlServer，需要做类型转换
                else if (DatabaseType.sqlserver.name().equalsIgnoreCase(importDatabaseType)) {
                    columnTypeAndSize = FieldTypeConvert.getConvertedFieldType(DatabaseType.sqlserver.name(), DatabaseType.mysql.name(), tableInfo);
                }
                sqlBuffer.append(columnTypeAndSize);
                //判断是否有注释
                if (!StringUtils.isEmpty(tableInfo.getString("remarks"))) {
                    sqlBuffer.append(" COMMENT " + "'" + tableInfo.getString("remarks") + "'");
                }
                //判断是否自增
                if ("YES".equals(tableInfo.getString("isAutoIncrement"))) {
                    sqlBuffer.append(" AUTO_INCREMENT ");
                }
                sqlBuffer.append(",");
            }
            //如果有主键，创建主键
            if (!StringUtils.isEmpty(obj.getString("primaryKey"))) {
                sqlBuffer.append(" PRIMARY KEY (" + obj.getString("primaryKey") + ")");
            }else {
                sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
            }
            sqlBuffer.append(")");
            DBUtil.createTable(sqlBuffer.toString(), exportParam);
        }
    }

    /**
     * oracle循环创建表
     * @param importDatabaseType
     * @param exportParam
     * @param selectedTablesFieldsJSONArray
     */
    public void createTableForOracle (String importDatabaseType, DatabaseParam exportParam,
                                      JSONArray selectedTablesFieldsJSONArray) {
        for (int i=0,iLen=selectedTablesFieldsJSONArray.size(); i<iLen; i++) {
            JSONObject obj = (JSONObject)selectedTablesFieldsJSONArray.get(i);
            DBUtil.dropTable(exportParam,  obj.getString("tableName")); //先删除表
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("CREATE TABLE " +  obj.getString("tableName") + "(");
            JSONArray fieldArray = obj.getJSONArray("fieldList");
            //如果有注释，创建注释
            Map<String,String> commentMap = new HashMap<>();
            for (int j=0,jLen=fieldArray.size(); j<jLen; j++) {
                JSONObject tableInfo = fieldArray.getJSONObject(j);
                sqlBuffer.append(tableInfo.getString("columnName") + " ");
                //类型转换
                String columnType = tableInfo.getString("columnType");
                int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
                int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
                String columnTypeAndSize = "";
                //如果是oracle本身，获取字段类型，需要指定size再指定size
                if (DatabaseType.oracle.name().equalsIgnoreCase(importDatabaseType)) {
                    columnTypeAndSize = FieldTypeConvert.getFieldTypeAndSize(DatabaseType.oracle.name(), columnType, columnSize, decimalDigits);
                } //如果是mysql,需要进行数据类型转换
                else if (DatabaseType.mysql.name().equalsIgnoreCase(importDatabaseType)) {//如果输入为mysql,需要类型转换
                    columnTypeAndSize =  FieldTypeConvert.getConvertedFieldType(DatabaseType.mysql.name(), DatabaseType.oracle.name(),tableInfo);
                } //如果是sqlServer,需要进行数据类型转换
                else if (DatabaseType.sqlserver.name().equalsIgnoreCase(importDatabaseType)) {
                    columnTypeAndSize = FieldTypeConvert.getConvertedFieldType(DatabaseType.sqlserver.name(), DatabaseType.oracle.name(), tableInfo);
                }
                sqlBuffer.append(columnTypeAndSize);
                if (obj.getString("primaryKey").equals(tableInfo.getString("columnName"))) {
                    sqlBuffer.append(" PRIMARY KEY ");
                }
                if (!StringUtils.isEmpty(tableInfo.getString("remarks"))) {
                    commentMap.put(tableInfo.getString("columnName"), tableInfo.getString("remarks"));//保存注释
                }
                sqlBuffer.append(",");
            }
            sqlBuffer.deleteCharAt(sqlBuffer.length() - 1).append(")");
            DBUtil.createTable(sqlBuffer.toString(), exportParam);
            //给表字段添加注释
            DBUtil.addFieldCommentsForOracle(commentMap, exportParam, obj.getString("tableName"));
        }
    }

    /**
     * sqlServer循环创建表
     * @param importDatabaseType
     * @param exportParam
     * @param selectedTablesFieldsJSONArray
     */
    public void createTableForSqlServer (String importDatabaseType, DatabaseParam exportParam,
                                      JSONArray selectedTablesFieldsJSONArray) {
        for (int i=0,iLen=selectedTablesFieldsJSONArray.size(); i<iLen; i++) {
            JSONObject obj = (JSONObject)selectedTablesFieldsJSONArray.get(i);
            StringBuffer sqlBuffer = new StringBuffer();
            DBUtil.dropTable(exportParam,  obj.getString("tableName")); //先删除表
            sqlBuffer.append("CREATE TABLE " +  obj.getString("tableName") + "(");
            JSONArray fieldArray = obj.getJSONArray("fieldList");
            //如果有注释，创建注释
            Map<String,String> commentMap = new HashMap<>();
            for (int j=0,jLen=fieldArray.size(); j<jLen; j++) {
                JSONObject tableInfo = fieldArray.getJSONObject(j);
                sqlBuffer.append(tableInfo.getString("columnName") + " ");
                //类型转换
                String columnType = tableInfo.getString("columnType");
                int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
                int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
                String columnTypeAndSize = "";
                //如果是sqlServer本身，获取字段类型，需要指定size再指定size
                if (DatabaseType.sqlserver.name().equalsIgnoreCase(importDatabaseType)) {
                    columnTypeAndSize = FieldTypeConvert.getFieldTypeAndSize(DatabaseType.sqlserver.name(), columnType, columnSize, decimalDigits);
                } //如果是mysql,需要进行数据类型转换
                else if (DatabaseType.mysql.name().equalsIgnoreCase(importDatabaseType)) {//如果输入为mysql,需要类型转换
                    columnTypeAndSize =  FieldTypeConvert.getConvertedFieldType(DatabaseType.mysql.name(), DatabaseType.sqlserver.name(),tableInfo);
                } //如果是oracle,需要进行数据类型转换
                else if (DatabaseType.oracle.name().equalsIgnoreCase(importDatabaseType)) {
                    columnTypeAndSize = FieldTypeConvert.getConvertedFieldType(DatabaseType.oracle.name(), DatabaseType.sqlserver.name(), tableInfo);
                }
                sqlBuffer.append(columnTypeAndSize);
                if (obj.getString("primaryKey").equals(tableInfo.getString("columnName"))) {
                    sqlBuffer.append(" PRIMARY KEY ");
                }
                if (!StringUtils.isEmpty(tableInfo.getString("remarks"))) {
                    commentMap.put(tableInfo.getString("columnName"), tableInfo.getString("remarks"));//保存注释
                }
                sqlBuffer.append(",");
            }
            sqlBuffer.deleteCharAt(sqlBuffer.length() - 1).append(")");
            DBUtil.createTable(sqlBuffer.toString(), exportParam);
            //给表字段添加注释
            DBUtil.addFieldCommentsForSqlServer(commentMap, exportParam, obj.getString("tableName"));
        }
    }

    /**
     * 循环保存数据到输出数据表中
     * @param froParam
     * @param toParam
     * @param selectedTablesFieldsJSONArray
     */
    public void handleBatchInsertData(DatabaseParam froParam, DatabaseParam toParam,
                                      JSONArray selectedTablesFieldsJSONArray, DataCollectionScheduleJob scheduleJob) {
        try {
            List<String> tableNameList = DBUtil.getTableNameList(froParam); //获取该库所有表名
            for (int i=0, iLen=tableNameList.size(); i<iLen; i++) {
                for (int j = 0, jLen=selectedTablesFieldsJSONArray.size(); j<jLen; j++) {
                    JSONObject obj = (JSONObject)selectedTablesFieldsJSONArray.get(j);
                    if (tableNameList.get(i).equals(obj.getString("tableName"))) {
                        //根据选择的字段，过滤掉未选择的字段
                        JSONArray fieldArray = obj.getJSONArray("fieldList");
                        String[] columns = new String[fieldArray.size()];
                        StringBuilder selectSqlBuilder = new StringBuilder();
                        selectSqlBuilder.append("SELECT ");
                        for (int k=0, kLen=fieldArray.size(); k<kLen; k++) {
                            columns[k] = fieldArray.getJSONObject(k).getString("columnName");
                            selectSqlBuilder.append(columns[k]).append(",");
                        }
                        selectSqlBuilder.deleteCharAt(selectSqlBuilder.length()-1).append(" FROM ").append(tableNameList.get(i));
                        //插入到输出数据表中
                        DBUtil.batchInsertToAnotherTable(froParam, toParam, tableNameList.get(i), selectSqlBuilder.toString(), 1000);
                        break;
                    }
                }
            }
        }catch (Exception e) {
            scheduleJob.setStatus(ScheduleJobStatus.FAIL.name());
            scheduleJobMapper.updateByPrimaryKey(scheduleJob);
        }
    }

    /**
     * 获取DatabaseParam对象
     */
    private DatabaseParam getDataBaseParam(DataCollectionDataSource dataSource) {
        DatabaseParam databaseParam = new DatabaseParam();
        databaseParam.setDriverClassName(dataSource.getDriverClassName());
        databaseParam.setLinkedUrl(dataSource.getLinkedUrl());
        databaseParam.setUsername(dataSource.getUsername());
        databaseParam.setPassword(dataSource.getPassword());
        return databaseParam;
    }
}
