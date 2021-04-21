package com.softline.mbg.mapper;

import com.softline.mbg.model.SysAdminRoleRelation;
import com.softline.mbg.model.SysAdminRoleRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysAdminRoleRelationMapper {
    long countByExample(SysAdminRoleRelationExample example);

    int deleteByExample(SysAdminRoleRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysAdminRoleRelation record);

    int insertSelective(SysAdminRoleRelation record);

    List<SysAdminRoleRelation> selectByExample(SysAdminRoleRelationExample example);

    SysAdminRoleRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysAdminRoleRelation record, @Param("example") SysAdminRoleRelationExample example);

    int updateByExample(@Param("record") SysAdminRoleRelation record, @Param("example") SysAdminRoleRelationExample example);

    int updateByPrimaryKeySelective(SysAdminRoleRelation record);

    int updateByPrimaryKey(SysAdminRoleRelation record);
}