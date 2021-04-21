package com.softline.quartz.enums;

/**
 * 定时任务状态
 * Created by dong ON 2020/12/15
 */
public enum  ScheduleJobStatus {
    CREATED, //已创建
    RUNNING, //运行中
    PAUSE, //暂停
    SUCCESS, //执行成功
    FAIL //执行失败
}
