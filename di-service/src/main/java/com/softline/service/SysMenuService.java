package com.softline.service;

import com.softline.dto.SysMenuNode;
import com.softline.mbg.model.SysMenu;

import java.util.List;

/**
 * 后台菜单管理Service
 * Created by dong on 2020/11/26.
 */
public interface SysMenuService {
    /**
     * 创建后台菜单
     */
    int create(SysMenu sysMenu);

    /**
     * 修改后台菜单
     */
    int update(Long id, SysMenu sysMenu);

    /**
     * 根据ID获取菜单详情
     */
    SysMenu getItem(Long id);

    /**
     * 根据ID删除菜单
     */
    int delete(Long id);

    /**
     * 分页查询后台菜单
     */
    List<SysMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<SysMenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    int updateHidden(Long id, Integer hidden);
}
