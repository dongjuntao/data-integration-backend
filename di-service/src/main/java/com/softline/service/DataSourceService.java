package com.softline.service;

import com.softline.mbg.model.DataCollectionDataSource;

import java.util.List;

/**
 * 数据源管理相关服务
 * Created by dong ON 2020/12/2
 */
public interface DataSourceService {
    /**
     * 添加数据源
     */
    int create(DataCollectionDataSource dataSource);
    /**
     * 修改数据源
     */
    int update(Long id, DataCollectionDataSource dataSource);

    /**
     * 删除数据源（支持单个和批量）
     */
    int delete(List<Long> ids);
    /**
     * 获取单个数据源详情
     * @param id
     * @return
     */
    DataCollectionDataSource getDataSource(Long id);

    /**
     * 分页获取数据源列表
     */
    List<DataCollectionDataSource> listDataSource(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 获取所有数据源列表（不分页）
     */
    List<DataCollectionDataSource> listAllDataSource(String status);


    /**
     * 修改数据源生命周期状态
     */
    int updateStatus(Long id, String status);

}
