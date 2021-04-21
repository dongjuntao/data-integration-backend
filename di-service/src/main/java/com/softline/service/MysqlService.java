package com.softline.service;

import com.softline.database.common.DatabaseParam;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by dong ON 2020/11/26
 */
public interface MysqlService {

    Map<String, Object>  getTableNamesAndFieldsByDataByDBName(DatabaseParam databaseParam) throws Exception;

    List<String> getTableNameList(DatabaseParam databaseParam) throws SQLException;

    Map<String, Object> getFieldsByTableName(String tableName, DatabaseParam databaseParam) throws SQLException;

    boolean isConnected(DatabaseParam databaseParam);
}
