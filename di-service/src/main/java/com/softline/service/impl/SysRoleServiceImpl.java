package com.softline.service.impl;

import com.github.pagehelper.PageHelper;
import com.softline.dao.SysRoleDao;
import com.softline.mbg.mapper.SysRoleMapper;
import com.softline.mbg.mapper.SysRoleMenuRelationMapper;
import com.softline.service.SysAdminCacheService;
import com.softline.service.SysRoleService;
import com.softline.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 后台角色管理Service实现类
 * Created by dong on 2020/11/26.
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuRelationMapper sysRoleMenuRelationMapper;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysAdminCacheService adminCacheService;
    @Override
    public int create(SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        sysRole.setAdminCount(0);
        sysRole.setSort(0);
        return sysRoleMapper.insert(sysRole);
    }

    @Override
    public int update(Long id, SysRole sysRole) {
        sysRole.setId(id);
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public int delete(List<Long> ids) {
        SysRoleExample example = new SysRoleExample();
        example.createCriteria().andIdIn(ids);
        int count = sysRoleMapper.deleteByExample(example);
        return count;
    }

    @Override
    public List<SysRole> list() {
        return sysRoleMapper.selectByExample(new SysRoleExample());
    }

    @Override
    public List<SysRole> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        SysRoleExample example = new SysRoleExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return sysRoleMapper.selectByExample(example);
    }

    @Override
    public List<SysMenu> getMenuList(Long adminId) {
        return sysRoleDao.getMenuList(adminId);
    }

    @Override
    public List<SysMenu> listMenu(Long roleId) {
        return sysRoleDao.getMenuListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        SysRoleMenuRelationExample example=new SysRoleMenuRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        sysRoleMenuRelationMapper.deleteByExample(example);
        //批量插入新关系
        for (Long menuId : menuIds) {
            SysRoleMenuRelation relation = new SysRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            sysRoleMenuRelationMapper.insert(relation);
        }
        return menuIds.size();
    }


}
