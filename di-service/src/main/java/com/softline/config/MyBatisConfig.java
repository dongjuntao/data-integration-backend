package com.softline.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 * Created by dong on 2020/11/25.
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.softline.mbg.mapper","com.softline.dao"})
public class MyBatisConfig {
}
