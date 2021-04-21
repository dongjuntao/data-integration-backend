package com.softline.mbg.mapper;

import com.softline.mbg.model.DataCollectionDataSource;
import com.softline.mbg.model.DataCollectionDataSourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataCollectionDataSourceMapper {
    long countByExample(DataCollectionDataSourceExample example);

    int deleteByExample(DataCollectionDataSourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataCollectionDataSource record);

    int insertSelective(DataCollectionDataSource record);

    List<DataCollectionDataSource> selectByExample(DataCollectionDataSourceExample example);

    DataCollectionDataSource selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataCollectionDataSource record, @Param("example") DataCollectionDataSourceExample example);

    int updateByExample(@Param("record") DataCollectionDataSource record, @Param("example") DataCollectionDataSourceExample example);

    int updateByPrimaryKeySelective(DataCollectionDataSource record);

    int updateByPrimaryKey(DataCollectionDataSource record);
}