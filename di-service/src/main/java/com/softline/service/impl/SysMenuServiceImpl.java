package com.softline.service.impl;

import com.github.pagehelper.PageHelper;
import com.softline.dto.SysMenuNode;
import com.softline.mbg.mapper.SysMenuMapper;
import com.softline.mbg.model.SysMenu;
import com.softline.mbg.model.SysMenuExample;
import com.softline.service.SysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单管理Service实现类
 * Created by dong on 2020/11/26.
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public int create(SysMenu sysMenu) {
        sysMenu.setCreateTime(new Date());
        updateLevel(sysMenu);
        return sysMenuMapper.insert(sysMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(SysMenu sysMenu) {
        if (sysMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            sysMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            SysMenu parentMenu = sysMenuMapper.selectByPrimaryKey(sysMenu.getParentId());
            if (parentMenu != null) {
                sysMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                sysMenu.setLevel(0);
            }
        }
    }

    @Override
    public int update(Long id, SysMenu sysMenu) {
        sysMenu.setId(id);
        updateLevel(sysMenu);
        return sysMenuMapper.updateByPrimaryKeySelective(sysMenu);
    }

    @Override
    public SysMenu getItem(Long id) {
        return sysMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        return sysMenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<SysMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        SysMenuExample example = new SysMenuExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return sysMenuMapper.selectByExample(example);
    }

    @Override
    public List<SysMenuNode> treeList() {
        List<SysMenu> menuList = sysMenuMapper.selectByExample(new SysMenuExample());
        List<SysMenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
        return result;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(id);
        sysMenu.setHidden(hidden);
        return sysMenuMapper.updateByPrimaryKeySelective(sysMenu);
    }

    /**
     * 将SysMenu转化为SysMenuNode并设置children属性
     */
    private SysMenuNode covertMenuNode(SysMenu sysMenu, List<SysMenu> menuList) {
        SysMenuNode node = new SysMenuNode();
        BeanUtils.copyProperties(sysMenu, node);
        List<SysMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(sysMenu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
