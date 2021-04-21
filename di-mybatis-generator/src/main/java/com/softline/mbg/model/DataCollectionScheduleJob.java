package com.softline.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class DataCollectionScheduleJob implements Serializable {
    @ApiModelProperty(value = "主键，任务id")
    private Long id;

    @ApiModelProperty(value = "定时任务名称")
    private String name;

    @ApiModelProperty(value = "数据采集任务id")
    private Long dataCollectionTaskId;

    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;

    @ApiModelProperty(value = "定时任务类型")
    private Integer type;

    @ApiModelProperty(value = "定时任务状态")
    private String status;

    @ApiModelProperty(value = "描述信息")
    private String description;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDataCollectionTaskId() {
        return dataCollectionTaskId;
    }

    public void setDataCollectionTaskId(Long dataCollectionTaskId) {
        this.dataCollectionTaskId = dataCollectionTaskId;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", dataCollectionTaskId=").append(dataCollectionTaskId);
        sb.append(", cronExpression=").append(cronExpression);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", description=").append(description);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}