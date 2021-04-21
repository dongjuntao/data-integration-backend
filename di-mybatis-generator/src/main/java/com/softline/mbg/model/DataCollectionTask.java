package com.softline.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class DataCollectionTask implements Serializable {
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "采集任务名称")
    private String taskName;

    @ApiModelProperty(value = "采集任务描述")
    private String taskDesc;

    @ApiModelProperty(value = "输入数据源id")
    private Long importDataSourceId;

    @ApiModelProperty(value = "输出数据源id")
    private Long exportDataSourceId;

    @ApiModelProperty(value = "是否需要配置字段映射，0:否；1:是")
    private Integer isNeedMapper;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "采集任务创建时间")
    private Date createTime;

    @ApiModelProperty(value = "已选择的输入数据源的库表字段信息")
    private String selectedTablesFields;

    @ApiModelProperty(value = "已选择的库表字段及映射")
    private String mapTablesFields;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Long getImportDataSourceId() {
        return importDataSourceId;
    }

    public void setImportDataSourceId(Long importDataSourceId) {
        this.importDataSourceId = importDataSourceId;
    }

    public Long getExportDataSourceId() {
        return exportDataSourceId;
    }

    public void setExportDataSourceId(Long exportDataSourceId) {
        this.exportDataSourceId = exportDataSourceId;
    }

    public Integer getIsNeedMapper() {
        return isNeedMapper;
    }

    public void setIsNeedMapper(Integer isNeedMapper) {
        this.isNeedMapper = isNeedMapper;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSelectedTablesFields() {
        return selectedTablesFields;
    }

    public void setSelectedTablesFields(String selectedTablesFields) {
        this.selectedTablesFields = selectedTablesFields;
    }

    public String getMapTablesFields() {
        return mapTablesFields;
    }

    public void setMapTablesFields(String mapTablesFields) {
        this.mapTablesFields = mapTablesFields;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskName=").append(taskName);
        sb.append(", taskDesc=").append(taskDesc);
        sb.append(", importDataSourceId=").append(importDataSourceId);
        sb.append(", exportDataSourceId=").append(exportDataSourceId);
        sb.append(", isNeedMapper=").append(isNeedMapper);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", selectedTablesFields=").append(selectedTablesFields);
        sb.append(", mapTablesFields=").append(mapTablesFields);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}