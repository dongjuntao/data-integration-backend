package com.softline.database.common;

import lombok.Data;

/**
 * Created by dong ON 2020/11/26
 */
@Data
public class DatabaseParam {
    /**
     * 数据库驱动名称
     */
    private String driverClassName;
    /**
     * 数据库地址
     */
    private String linkedUrl;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;

}
