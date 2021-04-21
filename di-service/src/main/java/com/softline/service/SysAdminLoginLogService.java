package com.softline.service;

import com.softline.mbg.model.SysAdminLoginLog;

import java.util.List;

/**
 * Created by dong ON 2020/11/25
 */
public interface SysAdminLoginLogService {

    /**分页获取管理员登录日志*/
    List<SysAdminLoginLog> list(String keyword, Integer pageSize, Integer pageNum);
}
