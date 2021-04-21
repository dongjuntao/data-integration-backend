package com.softline.service.impl;

import com.github.pagehelper.PageHelper;
import com.softline.mbg.mapper.SysAdminLoginLogMapper;
import com.softline.mbg.model.SysAdminLoginLog;
import com.softline.mbg.model.SysAdminLoginLogExample;
import com.softline.service.SysAdminLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by dong ON 2020/11/25
 */
@Service
public class SysAdminLoginLogServiceImpl implements SysAdminLoginLogService {

    @Autowired
    private SysAdminLoginLogMapper sysAdminLoginLogMapper;

    @Override
    public List<SysAdminLoginLog> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        SysAdminLoginLogExample example = new SysAdminLoginLogExample();
        SysAdminLoginLogExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andAdminNameLike("%" + keyword + "%");
        }
        return sysAdminLoginLogMapper.selectByExample(example);
    }
}
