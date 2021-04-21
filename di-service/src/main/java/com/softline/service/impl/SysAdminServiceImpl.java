package com.softline.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.softline.bo.AdminUserDetails;
import com.softline.common.exception.Asserts;
import com.softline.common.util.RequestUtil;
import com.softline.dao.SysAdminRoleRelationDao;
import com.softline.dto.SysAdminParam;
import com.softline.dto.UpdateAdminPasswordParam;
import com.softline.mbg.mapper.SysAdminLoginLogMapper;
import com.softline.mbg.mapper.SysAdminRoleRelationMapper;
import com.softline.mbg.mapper.SysAdminUserMapper;
import com.softline.security.util.JwtTokenUtil;
import com.softline.service.SysAdminCacheService;
import com.softline.service.SysAdminService;
import com.softline.mbg.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SysAdminService实现类
 * Created by dong on 2020/11/26.
 */
@Service
public class SysAdminServiceImpl implements SysAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysAdminUserMapper sysAdminUserMapper;
    @Autowired
    private SysAdminRoleRelationMapper sysAdminRoleRelationMapper;
    @Autowired
    private SysAdminRoleRelationDao adminRoleRelationDao;
    @Autowired
    private SysAdminLoginLogMapper sysAdminLoginLogMapper;
    @Autowired
    private SysAdminCacheService adminCacheService;

    @Override
    public SysAdminUser getAdminByUsername(String username) {
        SysAdminUser sysAdminUser = adminCacheService.getAdmin(username);
        if(sysAdminUser!=null) return  sysAdminUser;
        SysAdminUserExample example = new SysAdminUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysAdminUser> adminList = sysAdminUserMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            sysAdminUser = adminList.get(0);
            adminCacheService.setAdmin(sysAdminUser);
            return sysAdminUser;
        }
        return null;
    }

    @Override
    public SysAdminUser register(SysAdminParam sysAdminParam) {
        SysAdminUser sysAdmin = new SysAdminUser();
        BeanUtils.copyProperties(sysAdminParam, sysAdmin);
        sysAdmin.setCreateTime(new Date());
        sysAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        SysAdminUserExample example = new SysAdminUserExample();
        example.createCriteria().andUsernameEqualTo(sysAdmin.getUsername());
        List<SysAdminUser> sysAdminList = sysAdminUserMapper.selectByExample(example);
        if (sysAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(sysAdmin.getPassword());
        sysAdmin.setPassword(encodePassword);
        sysAdminUserMapper.insert(sysAdmin);
        return sysAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("密码不正确");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
//            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        SysAdminUser sysAdminUser = getAdminByUsername(username);
        if(sysAdminUser==null) return;
        SysAdminLoginLog loginLog = new SysAdminLoginLog();
        loginLog.setAdminId(sysAdminUser.getId());
        loginLog.setAdminName(username);
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(RequestUtil.getRequestIp(request));
        loginLog.setAddress(RequestUtil.getAddressByIP(loginLog.getIp()));
        loginLog.setUserAgent(RequestUtil.getBrowserName(request.getHeader("user-agent")));
        sysAdminLoginLogMapper.insert(loginLog);
    }

    /**
     * 根据用户名修改登录时间
     */
    private void updateLoginTimeByUsername(String username) {
        SysAdminUser record = new SysAdminUser();
        record.setLoginTime(new Date());
        SysAdminUserExample example = new SysAdminUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        sysAdminUserMapper.updateByExampleSelective(record, example);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public SysAdminUser getItem(Long id) {
        return sysAdminUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysAdminUser> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        SysAdminUserExample example = new SysAdminUserExample();
        SysAdminUserExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andUsernameLike("%" + keyword + "%");
            example.or(example.createCriteria().andNickNameLike("%" + keyword + "%"));
        }
        return sysAdminUserMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, SysAdminUser sysAdminUser) {
        sysAdminUser.setId(id);
        SysAdminUser rawAdmin = sysAdminUserMapper.selectByPrimaryKey(id);
        if(rawAdmin.getPassword().equals(sysAdminUser.getPassword())){
            //与原加密密码相同的不需要修改
            sysAdminUser.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(sysAdminUser.getPassword())){
                sysAdminUser.setPassword(null);
            }else{
                sysAdminUser.setPassword(passwordEncoder.encode(sysAdminUser.getPassword()));
            }
        }
        int count = sysAdminUserMapper.updateByPrimaryKeySelective(sysAdminUser);
        adminCacheService.deleteAdmin(id);
        return count;
    }

    @Override
    public int delete(Long id) {
        adminCacheService.deleteAdmin(id);
        int count = sysAdminUserMapper.deleteByPrimaryKey(id);
        return count;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        SysAdminRoleRelationExample adminRoleRelationExample = new SysAdminRoleRelationExample();
        adminRoleRelationExample.createCriteria().andAdminIdEqualTo(adminId);
        sysAdminRoleRelationMapper.deleteByExample(adminRoleRelationExample);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysAdminRoleRelation roleRelation = new SysAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationDao.insertList(list);
        }
        return count;
    }

    @Override
    public List<SysRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        SysAdminUserExample example = new SysAdminUserExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<SysAdminUser> adminList = sysAdminUserMapper.selectByExample(example);
        if(CollUtil.isEmpty(adminList)){
            return -2;
        }
        SysAdminUser sysAdmin = adminList.get(0);
        if(!passwordEncoder.matches(param.getOldPassword(),sysAdmin.getPassword())){
            return -3;
        }
        sysAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        sysAdminUserMapper.updateByPrimaryKey(sysAdmin);
        adminCacheService.deleteAdmin(sysAdmin.getId());
        return 1;
    }

    /***登录时获取用户相关信息，可以通过扩展AdminUserDetails来增加需要的用户信息*/
    @Override
    public UserDetails loadUserByUsername(String username){
        //获取用户信息
        SysAdminUser sysAdminUser = getAdminByUsername(username);
        if (sysAdminUser != null) {
            return new AdminUserDetails(sysAdminUser);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
