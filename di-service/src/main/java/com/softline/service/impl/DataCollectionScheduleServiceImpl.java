package com.softline.service.impl;

import com.github.pagehelper.PageHelper;
import com.softline.mbg.mapper.DataCollectionScheduleJobMapper;
import com.softline.mbg.model.DataCollectionScheduleJob;
import com.softline.mbg.model.DataCollectionScheduleJobExample;
import com.softline.quartz.enums.ScheduleJobStatus;
import com.softline.quartz.util.DataCollectionScheduleUtil;
import com.softline.service.DataCollectionScheduleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.quartz.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by dong ON 2020/12/15
 */
@Service
public class DataCollectionScheduleServiceImpl implements DataCollectionScheduleService {

    @Autowired
    private DataCollectionScheduleJobMapper scheduleJobMapper;

    @Autowired
    private Scheduler scheduler;

    /***
     * 定时任务管理
     * @param scheduleJob
     * @return
     */
    @Override
    public int create(DataCollectionScheduleJob scheduleJob) {
        scheduleJob.setCreateTime(new Date());
        int count = scheduleJobMapper.insert(scheduleJob);
        //quartz创建定时任务
        DataCollectionScheduleUtil.createScheduleJob(scheduler, scheduleJob);
        return count;
    }

    /**
     * 修改定时任务
     * @param jobId
     * @param scheduleJob
     * @return
     */
    @Override
    public int update(Long jobId, DataCollectionScheduleJob scheduleJob) {
        //查不到就无法修改
        DataCollectionScheduleJob collectionDataSource = scheduleJobMapper.selectByPrimaryKey(jobId);
        if (collectionDataSource == null) {
            return 0;
        }
        int count = scheduleJobMapper.updateByPrimaryKey(scheduleJob);
        scheduleJob.setId(jobId);
        //quartz修改定时任务
        DataCollectionScheduleUtil.updateScheduleJob(scheduler, scheduleJob);
        return count;
    }

    /**
     * 删除定时任务
     * @param jobIds
     * @return
     */
    @Override
    public int delete(List<Long> jobIds) {
        DataCollectionScheduleJobExample example = new DataCollectionScheduleJobExample();
        example.createCriteria().andIdIn(jobIds);
        int count = scheduleJobMapper.deleteByExample(example);
        //quartz循环删除定时任务
        for(Long jobId : jobIds){
            DataCollectionScheduleUtil.deleteScheduleJob(scheduler, jobId);
        }
        return count;
    }

    /***
     * 获取一个定时任务详情
     * @param jobId
     * @return
     */
    @Override
    public DataCollectionScheduleJob getDataCollectionScheduleJob(Long jobId) {
        return scheduleJobMapper.selectByPrimaryKey(jobId);
    }

    /**
     * 获取定时任务列表（分页）
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public List<DataCollectionScheduleJob> listDataCollectionScheduleJob(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        DataCollectionScheduleJobExample example = new DataCollectionScheduleJobExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return scheduleJobMapper.selectByExample(example);
    }

    /**
     * 修改定时任务生命周期状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateStatus(Long id, String status) {
        DataCollectionScheduleJob collectionScheduleJob = scheduleJobMapper.selectByPrimaryKey(id);
        if (collectionScheduleJob == null) {
            return 0;
        }
        collectionScheduleJob.setStatus(status);
        return scheduleJobMapper.updateByPrimaryKey(collectionScheduleJob);
    }

    /**
     * 立即执行
     * @param jobIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(List<Long> jobIds) {
        for(Long jobId : jobIds){
            DataCollectionScheduleUtil.run(scheduler, this.getDataCollectionScheduleJob(jobId));
            updateStatus(jobId, ScheduleJobStatus.RUNNING.name());
        }
    }

    /**
     * 暂停
     * @param jobIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(List<Long> jobIds) {
        for(Long jobId : jobIds){
            DataCollectionScheduleUtil.pauseJob(scheduler, jobId);
            updateStatus(jobId, ScheduleJobStatus.PAUSE.name());
        }
    }

    /**
     * 恢复
     * @param jobIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(List<Long> jobIds) {
        for(Long jobId : jobIds){
            DataCollectionScheduleUtil.resumeJob(scheduler, jobId);
            updateStatus(jobId, ScheduleJobStatus.RUNNING.name());
        }
    }
}
