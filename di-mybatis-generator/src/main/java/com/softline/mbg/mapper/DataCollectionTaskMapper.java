package com.softline.mbg.mapper;

import com.softline.mbg.model.DataCollectionTask;
import com.softline.mbg.model.DataCollectionTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataCollectionTaskMapper {
    long countByExample(DataCollectionTaskExample example);

    int deleteByExample(DataCollectionTaskExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataCollectionTask record);

    int insertSelective(DataCollectionTask record);

    List<DataCollectionTask> selectByExampleWithBLOBs(DataCollectionTaskExample example);

    List<DataCollectionTask> selectByExample(DataCollectionTaskExample example);

    DataCollectionTask selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataCollectionTask record, @Param("example") DataCollectionTaskExample example);

    int updateByExampleWithBLOBs(@Param("record") DataCollectionTask record, @Param("example") DataCollectionTaskExample example);

    int updateByExample(@Param("record") DataCollectionTask record, @Param("example") DataCollectionTaskExample example);

    int updateByPrimaryKeySelective(DataCollectionTask record);

    int updateByPrimaryKeyWithBLOBs(DataCollectionTask record);

    int updateByPrimaryKey(DataCollectionTask record);
}