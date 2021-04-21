package com.softline.service;

import com.softline.mbg.model.SysMenu;
import com.softline.mbg.model.SysRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台角色管理Service
 * Created by dong on 2020/11/26.
 */
public interface SysRoleService {
    /**
     * 添加角色
     */
    int create(SysRole sysRole);

    /**
     * 修改角色信息
     */
    int update(Long id, SysRole sysRole);

    /**
     * 批量删除角色
     */
    int delete(List<Long> ids);

    /**
     * 获取所有角色列表
     */
    List<SysRole> list();

    /**
     * 分页获取角色列表
     */
    List<SysRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<SysMenu> getMenuList(Long adminId);

    /**
     * 获取角色相关菜单
     */
    List<SysMenu> listMenu(Long roleId);

    /**
     * 给角色分配菜单
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

}
