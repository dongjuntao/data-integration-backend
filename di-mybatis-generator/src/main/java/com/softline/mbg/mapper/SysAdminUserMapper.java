package com.softline.mbg.mapper;

import com.softline.mbg.model.SysAdminUser;
import com.softline.mbg.model.SysAdminUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysAdminUserMapper {
    long countByExample(SysAdminUserExample example);

    int deleteByExample(SysAdminUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysAdminUser record);

    int insertSelective(SysAdminUser record);

    List<SysAdminUser> selectByExample(SysAdminUserExample example);

    SysAdminUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysAdminUser record, @Param("example") SysAdminUserExample example);

    int updateByExample(@Param("record") SysAdminUser record, @Param("example") SysAdminUserExample example);

    int updateByPrimaryKeySelective(SysAdminUser record);

    int updateByPrimaryKey(SysAdminUser record);
}