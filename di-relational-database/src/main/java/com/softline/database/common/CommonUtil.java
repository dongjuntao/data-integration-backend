package com.softline.database.common;

import com.softline.common.exception.ApiException;
import com.softline.database.enums.DatabaseType;

import java.sql.*;

/**
 * DBUtil 公共的JDBC工具类
 * Created by dong ON 2020/11/27
 */
public class CommonUtil {

    /**
     * 获取一个数据库连接
     * @param databaseParam
     * @return
     * @throws Exception
     */
    public static Connection getConnection(DatabaseParam databaseParam) throws ApiException {
        return DataSourceFactory.getConnection(databaseParam);
    }

    /**
     * 关闭连接
     * @param conn
     * @param pre
     * @param rs
     */
    public static void close(Connection conn, Statement statement, PreparedStatement pre, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (pre != null) {
            try {
                pre.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取处理后的数据库相关参数
     * @param param
     * @return
     */
    public static DatabaseParam getHandledDatabaseParam(DatabaseParam param) {
        String driverClassName = param.getDriverClassName(), linkedUrl = param.getLinkedUrl();
        if (driverClassName.indexOf(DatabaseType.mysql.name()) != -1 && linkedUrl.indexOf("rewriteBatchedStatements")==-1) {
            if (linkedUrl.indexOf("?") != -1) param.setLinkedUrl(linkedUrl + "&rewriteBatchedStatements=true");
            else param.setLinkedUrl(linkedUrl + "?rewriteBatchedStatements=true");
        }
        return param;
    }

    /**
     * 根据总数量获取页数
     * @param totalCount
     * @return
     */
    public static int getPageCount(int totalCount) {
        int pageCount, tmpPage;
        pageCount = totalCount / 1000;
        tmpPage = totalCount % 1000 == 0 ? 0 : 1;
        pageCount = pageCount + tmpPage;
        return pageCount;
    }

    /**
     * 获取分页sql
     * @return
     */
    public static String getPaginationSql(DatabaseParam param, String baseSql, int pageNo, int pageSize) {
        //mysql分页
        StringBuilder newSql = new StringBuilder();
        if (param.getDriverClassName().indexOf(DatabaseType.mysql.name()) != -1) {
            newSql.append(baseSql).append(" LIMIT ").append(pageNo*pageSize).append(",").append(pageSize);
        }
        //oracle分页
        else if (param.getDriverClassName().indexOf(DatabaseType.oracle.name()) != -1) {
            newSql = new StringBuilder().append(baseSql.substring(0, baseSql.indexOf("FROM"))).append(" FROM(");
            newSql.append(" SELECT temp.* ,ROWNUM RN" +" FROM ( ");
            newSql.append(baseSql);
            newSql.append(" ) temp WHERE ROWNUM <= " + (pageNo*pageSize+pageSize));
            newSql.append(" ) WHERE RN > " + pageNo*pageSize);
        }
        //sqlServer分页
        else if (param.getDriverClassName().indexOf(DatabaseType.sqlserver.name()) != -1) {
            newSql = new StringBuilder().append(baseSql.substring(0, baseSql.indexOf("FROM"))).append(" FROM(");
            newSql.append("SELECT ROW_NUMBER() OVER (ORDER BY TEMPCOLUMN) TEMPROWNUMBER,* FROM ( SELECT TOP ");
            newSql.append(pageNo*pageSize + pageSize);
            newSql.append(" TEMPCOLUMN = 0, ");
            newSql.append(baseSql.substring(6));
            newSql.append(") t ) tt WHERE TEMPROWNUMBER > ");
            newSql.append(pageNo*pageSize);
        }
        return newSql.toString();
    }

    /**
     * 组装插入sql
     * @param insertSqlBuilder
     * @param tableName
     * @param size
     * @return
     */
    public static StringBuilder getInsertSql(StringBuilder insertSqlBuilder, String tableName, int size) {
        insertSqlBuilder.append("INSERT INTO " + tableName).append(" VALUES(");
        String link = "";
        for (int i=0,len=size; i<len; i++) {
            insertSqlBuilder.append(link).append("?");
            link=",";
        }
        insertSqlBuilder.append(")");
        return insertSqlBuilder;
    }

}
