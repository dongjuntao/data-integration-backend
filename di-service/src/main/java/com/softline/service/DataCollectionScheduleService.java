package com.softline.service;

import com.softline.mbg.model.DataCollectionScheduleJob;

import java.util.List;

/**
 * 定时任务采集service
 * Created by dong ON 2020/12/15
 */
public interface DataCollectionScheduleService {

    /**
     * 添加定时任务
     */
    int create(DataCollectionScheduleJob scheduleJob);
    /**
     * 修改定时任务
     */
    int update(Long jobId, DataCollectionScheduleJob scheduleJob);

    /**
     * 删除定时（支持单个和批量）
     */
    int delete(List<Long> jobIds);
    /**
     * 获取单个数据定时任务
     * @param jobId
     * @return
     */
    DataCollectionScheduleJob getDataCollectionScheduleJob(Long jobId);

    /**
     * 分页获取数据采集任务列表
     */
    List<DataCollectionScheduleJob> listDataCollectionScheduleJob(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改定时任务生命周期状态
     */
    int updateStatus(Long id, String status);

    /**
     * 立即执行
     */
    void run(List<Long> jobIds);

    /**
     * 暂停运行
     */
    void pause(List<Long> jobIds);

    /**
     * 恢复运行
     */
    void resume(List<Long> jobIds);
}
