package com.softline.service;

import com.softline.dto.SysAdminParam;
import com.softline.dto.UpdateAdminPasswordParam;
import com.softline.mbg.model.SysAdminUser;
import com.softline.mbg.model.SysRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台管理员Service
 * Created by dong on 2020/11/26.
 */
public interface SysAdminService {
    /**
     * 根据用户名获取后台管理员
     */
    SysAdminUser getAdminByUsername(String username);

    /**
     * 注册功能
     */
    SysAdminUser register(SysAdminParam sysAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username,String password);

    /**
     * 刷新token的功能
     * @param oldToken 旧的token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户id获取用户
     */
    SysAdminUser getItem(Long id);

    /**
     * 根据用户名或昵称分页查询用户
     */
    List<SysAdminUser> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    int update(Long id, SysAdminUser admin);

    /**
     * 删除指定用户
     */
    int delete(Long id);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对于角色
     */
    List<SysRole> getRoleList(Long adminId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);
}
