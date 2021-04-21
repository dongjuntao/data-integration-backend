package com.softline.service.impl;

import com.softline.database.mysql.DBUtil;
import com.softline.database.common.DatabaseParam;
import com.softline.service.MysqlService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by dong ON 2020/11/26
 */
@Service
public class MysqlServiceImpl implements MysqlService {

    @Override
    public  Map<String, Object>  getTableNamesAndFieldsByDataByDBName(DatabaseParam databaseParam) throws Exception{
        return DBUtil.getTableNamesAndFieldsByDataByDBName(databaseParam);
    }

    @Override
    public List<String> getTableNameList(DatabaseParam databaseParam) throws SQLException {
        return DBUtil.getTableNameList(databaseParam);
    }

    @Override
    public Map<String, Object> getFieldsByTableName(String tableName, DatabaseParam databaseParam) throws SQLException{
        return DBUtil.getFieldsByTableName(tableName, databaseParam);
    }

    /**测试数据库是否连通*/
    @Override
    public boolean isConnected(DatabaseParam databaseParam) {
        return DBUtil.isConnected(databaseParam);
    }
}
