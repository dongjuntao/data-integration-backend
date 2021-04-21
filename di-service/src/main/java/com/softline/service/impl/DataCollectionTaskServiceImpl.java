package com.softline.service.impl;

import com.github.pagehelper.PageHelper;
import com.softline.mbg.mapper.DataCollectionTaskMapper;
import com.softline.mbg.model.DataCollectionTask;
import com.softline.mbg.model.DataCollectionTaskExample;
import com.softline.service.DataCollectionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 数据采集service
 * Created by dong ON 2020/12/5
 */
@Service
public class DataCollectionTaskServiceImpl implements DataCollectionTaskService {

    @Autowired
    private DataCollectionTaskMapper dataCollectionTaskMapper;

    /***
     * 添加数据采集任务
     * @param dataCollectionTask
     * @return
     */
    @Override
    public int create(DataCollectionTask dataCollectionTask) {
        dataCollectionTask.setCreateTime(new Date());
        return dataCollectionTaskMapper.insert(dataCollectionTask);
    }

    /**
     * 修改数据采集任务
     * @param id
     * @param dataCollectionTask
     * @return
     */
    @Override
    public int update(Long id, DataCollectionTask dataCollectionTask) {
        //查不到就无法修改
        DataCollectionTask collectionTask = dataCollectionTaskMapper.selectByPrimaryKey(id);
        if (collectionTask == null) {
            return 0;
        }
        dataCollectionTask.setId(id);
        return dataCollectionTaskMapper.updateByPrimaryKeyWithBLOBs(dataCollectionTask);
    }

    /**
     * 删除数据采集任务（支持批量）
     * @param ids
     * @return
     */
    @Override
    public int delete(List<Long> ids) {
        DataCollectionTaskExample example = new DataCollectionTaskExample();
        example.createCriteria().andIdIn(ids);
        return dataCollectionTaskMapper.deleteByExample(example);
    }

    /**
     * 查询单个数据采集任务详情
     * @param id
     * @return
     */
    @Override
    public DataCollectionTask getDataCollectionTask(Long id) {
        return dataCollectionTaskMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询数据采集任务列表
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public List<DataCollectionTask> listDataCollectionTask(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        DataCollectionTaskExample example = new DataCollectionTaskExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.createCriteria().andTaskNameLike("%" + keyword + "%");
        }
        return dataCollectionTaskMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 获取所有数据采集任务（不分页）
     * @param status
     * @return
     */
    @Override
    public List<DataCollectionTask> listAllDataCollectionTask(String status) {
        DataCollectionTaskExample example = new DataCollectionTaskExample();
        if (!StringUtils.isEmpty(status)) {
            example.createCriteria().andStatusEqualTo(status);
        }
        return dataCollectionTaskMapper.selectByExample(example);
    }

    /**
     * 修改数据采集任务生命周期状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateStatus(Long id, String status) {
        //查不到就无法修改
        DataCollectionTask dataCollectionTask = dataCollectionTaskMapper.selectByPrimaryKey(id);
        if (dataCollectionTask == null) {
            return 0;
        }
        dataCollectionTask.setStatus(status);
        return dataCollectionTaskMapper.updateByPrimaryKey(dataCollectionTask);
    }
}
