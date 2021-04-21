package com.softline.mbg.mapper;

import com.softline.mbg.model.DataCollectionScheduleJob;
import com.softline.mbg.model.DataCollectionScheduleJobExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataCollectionScheduleJobMapper {
    long countByExample(DataCollectionScheduleJobExample example);

    int deleteByExample(DataCollectionScheduleJobExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataCollectionScheduleJob record);

    int insertSelective(DataCollectionScheduleJob record);

    List<DataCollectionScheduleJob> selectByExample(DataCollectionScheduleJobExample example);

    DataCollectionScheduleJob selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataCollectionScheduleJob record, @Param("example") DataCollectionScheduleJobExample example);

    int updateByExample(@Param("record") DataCollectionScheduleJob record, @Param("example") DataCollectionScheduleJobExample example);

    int updateByPrimaryKeySelective(DataCollectionScheduleJob record);

    int updateByPrimaryKey(DataCollectionScheduleJob record);
}