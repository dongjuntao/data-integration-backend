package com.softline.service;

import com.softline.mbg.model.DataCollectionTask;

import java.util.List;

/**
 * 数据采集任务接口
 * Created by dong ON 2020/12/5
 */
public interface DataCollectionTaskService {

    /**
     * 添加数据采集任务
     */
    int create(DataCollectionTask dataCollectionTask);
    /**
     * 修改数据采集任务
     */
    int update(Long id, DataCollectionTask dataCollectionTask);

    /**
     * 删除数据采集任务（支持单个和批量）
            */
    int delete(List<Long> ids);
    /**
     * 获取单个数据采集任务详情
     * @param id
     * @return
     */
    DataCollectionTask getDataCollectionTask(Long id);

    /**
     * 分页获取数据采集任务列表
     */
    List<DataCollectionTask> listDataCollectionTask(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 获取所有数据源列表（不分页）
     */
    List<DataCollectionTask> listAllDataCollectionTask(String status);

    /**
     * 修改数据采集任务生命周期状态
     */
    int updateStatus(Long id, String status);


}
