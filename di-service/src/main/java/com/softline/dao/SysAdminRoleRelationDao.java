package com.softline.dao;

import com.softline.mbg.model.SysAdminRoleRelation;
import com.softline.mbg.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义后台用户与角色管理Dao
 * Created by dong on 2020/11/26.
 */
public interface SysAdminRoleRelationDao {
    /**
     * 批量插入用户角色关系
     */
    int insertList(@Param("list") List<SysAdminRoleRelation> adminRoleRelationList);

    /**
     * 获取用于所有角色
     */
    List<SysRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
