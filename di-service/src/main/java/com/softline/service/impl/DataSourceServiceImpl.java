package com.softline.service.impl;

import com.github.pagehelper.PageHelper;
import com.softline.mbg.mapper.DataCollectionDataSourceMapper;
import com.softline.mbg.model.DataCollectionDataSource;
import com.softline.mbg.model.DataCollectionDataSourceExample;
import com.softline.service.DataSourceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 数据源管理相关服务实现类
 * Created by dong ON 2020/12/2
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataCollectionDataSourceMapper dataSourceMapper;

    /**
     * 添加数据源
     * @param dataSource
     * @return
     */
    @Override
    public int create(DataCollectionDataSource dataSource) {
        dataSource.setCreateTime(new Date());
        return dataSourceMapper.insert(dataSource);
    }

    /**
     * 更新数据源
     * @param id
     * @param dataSource
     * @return
     */
    @Override
    public int update(Long id, DataCollectionDataSource dataSource) {
        //查不到就无法修改
        DataCollectionDataSource collectionDataSource = dataSourceMapper.selectByPrimaryKey(id);
        if (collectionDataSource == null) {
            return 0;
        }
        dataSource.setId(id);
        return dataSourceMapper.updateByPrimaryKey(dataSource);
    }

    /**
     * 删除数据源（支持单个和批量）
     * @param ids
     * @return
     */
    @Override
    public int delete(List<Long> ids) {
        DataCollectionDataSourceExample example = new DataCollectionDataSourceExample();
        example.createCriteria().andIdIn(ids);
        return dataSourceMapper.deleteByExample(example);
    }

    /**
     * 查询单个数据详情
     * @param id
     * @return
     */
    @Override
    public DataCollectionDataSource getDataSource(Long id) {
        return dataSourceMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询数据源列表
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public List<DataCollectionDataSource> listDataSource(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        DataCollectionDataSourceExample example = new DataCollectionDataSourceExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return dataSourceMapper.selectByExample(example);
    }

    /***
     * 获取所有数据源列表（不分页）
     * @param status
     * @return
     */
    @Override
    public List<DataCollectionDataSource> listAllDataSource(String status) {
        DataCollectionDataSourceExample example = new DataCollectionDataSourceExample();
        if (!StringUtils.isEmpty(status)) {
            example.createCriteria().andStatusEqualTo(status);
        }
        return dataSourceMapper.selectByExample(example);
    }

    /**
     * 修改数据源生命周期状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateStatus(Long id, String status) {
        //查不到就无法修改
        DataCollectionDataSource collectionDataSource = dataSourceMapper.selectByPrimaryKey(id);
        if (collectionDataSource == null) {
            return 0;
        }
        collectionDataSource.setStatus(status);
        return dataSourceMapper.updateByPrimaryKey(collectionDataSource);
    }
}
