package com.softline.quartz.config;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dong ON 2020/12/16
 */
public class Constant {

    //关系型数据源定时任务job名称前缀
    public static final String DB_JOB_NAME = "DB_JOB_";

    //关系型数据库数据采集定时任务分布式锁key前缀
    public static final String DB_COLLECTION_REDIS_KEY_PREFIX = "db-data-collection-job";
}
