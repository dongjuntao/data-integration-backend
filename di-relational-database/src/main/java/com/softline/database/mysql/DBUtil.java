package com.softline.database.mysql;

import com.softline.database.common.CommonUtil;
import com.softline.database.common.DatabaseParam;
import com.softline.database.enums.DatabaseType;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 数据库JDBC连接工具类
 * Created by dong on 2020/11/27.
 */
public class DBUtil {

    /**
     * the pattern of limit
     */
    private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    /**
     * 执行删除操作
     * @param tableName 要删除的表名
     * @param whereMap  删除的条件
     * @return 影响的行数
     * @throws SQLException SQL执行异常
     */
    public static int delete(String tableName, Map<String, Object> whereMap,
                             DatabaseParam databaseParam) throws SQLException {
        /**准备删除的sql语句**/
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(tableName);

        /**更新的条件:要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder whereSql = new StringBuilder();
        Object[] bindArgs = null;
        if (whereMap != null && whereMap.size() > 0) {
            bindArgs = new Object[whereMap.size()];
            whereSql.append(" WHERE ");
            /**获取数据库插入的Map的键值对的值**/
            Set<String> keySet = whereMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                whereSql.append(i == 0 ? "" : " AND ");
                whereSql.append(key + " = ? ");
                bindArgs[i] = whereMap.get(key);
                i++;
            }
            sql.append(whereSql);
        }
        return executeUpdate(sql.toString(), bindArgs, databaseParam);
    }

    /**
     * 执行更新操作
     * @param tableName 表名
     * @param valueMap  要更改的值
     * @param whereMap  条件
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int update(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap,
                             DatabaseParam databaseParam) throws SQLException {
        /**获取数据库插入的Map的键值对的值**/
        Set<String> keySet = valueMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        /**开始拼插入的sql语句**/
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        /**要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder columnSql = new StringBuilder();
        int i = 0;
        List<Object> objects = new ArrayList<>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(key + " = ? ");
            objects.add(valueMap.get(key));
            i++;
        }
        sql.append(columnSql);
        /**更新的条件:要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder whereSql = new StringBuilder();
        int j = 0;
        if (whereMap != null && whereMap.size() > 0) {
            whereSql.append(" WHERE ");
            iterator = whereMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                whereSql.append(j == 0 ? "" : " AND ");
                whereSql.append(key + " = ? ");
                objects.add(whereMap.get(key));
                j++;
            }
            sql.append(whereSql);
        }
        return executeUpdate(sql.toString(), objects.toArray(), databaseParam);
    }

    /**
     * 执行sql通过 Map<String, Object>限定查询条件查询
     *
     * @param tableName 表名
     * @param whereMap  where条件
     * @return List<Map<String, Object>>
     * @throws SQLException
     */
    public static List<Map<String, Object>> query(String tableName, Map<String, Object> whereMap,
                                                  DatabaseParam databaseParam) throws Exception {
        String whereClause = "";
        Object[] whereArgs = null;
        if (whereMap != null && whereMap.size() > 0) {
            Iterator<String> iterator = whereMap.keySet().iterator();
            whereArgs = new Object[whereMap.size()];
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                whereClause += (i == 0 ? "" : " AND ");
                whereClause += (key + " = ? ");
                whereArgs[i] = whereMap.get(key);
                i++;
            }
        }
        return query(tableName, false, null, whereClause, whereArgs,
                null, null, null, null,databaseParam);
    }

    /**
     * 执行全部结构的sql查询
     *
     * @param tableName     表名
     * @param distinct      去重
     * @param columns       要查询的列名
     * @param selection     where条件
     * @param selectionArgs where条件中占位符中的值
     * @param groupBy       分组
     * @param having        筛选
     * @param orderBy       排序
     * @param limit         分页
     * @return List<Map<String, Object>>
     * @throws SQLException
     */
    public static List<Map<String, Object>> query(String tableName,
                                                  boolean distinct,
                                                  String[] columns,
                                                  String selection,
                                                  Object[] selectionArgs,
                                                  String groupBy,
                                                  String having,
                                                  String orderBy,
                                                  String limit,
                                                  DatabaseParam databaseParam) throws SQLException {
        String sql = buildQueryString(distinct, tableName, columns, selection, groupBy, having, orderBy, limit);
        return executeQuery(sql, selectionArgs, databaseParam);

    }

    /**
     * 组装sql
     * @param distinct
     * @param tables
     * @param columns
     * @param where
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    private static String buildQueryString(
            boolean distinct, String tables, String[] columns, String where,
            String groupBy, String having, String orderBy, String limit) {
        if (isEmpty(groupBy) && !isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(120);
        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            appendColumns(query, columns);
        } else {
            query.append(" * ");
        }
        query.append("FROM ");
        query.append(tables);
        if (!StringUtils.isEmpty(where)) {
            appendClause(query, " WHERE ", where);
        }
        if (!StringUtils.isEmpty(groupBy)) {
            appendClause(query, " GROUP BY ", groupBy);
        }
        if (!StringUtils.isEmpty(having)) {
            appendClause(query, " HAVING ", having);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            appendClause(query, " ORDER BY ", orderBy);
        }
        if (!StringUtils.isEmpty(limit)) {
            appendClause(query, " LIMIT ", limit);
        }
        return query.toString();
    }

    /**
     * Add the names that are non-null in columns to s, separating
     * them with commas.
     */
    private static void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;
        for (int i = 0; i < n; i++) {
            String column = columns[i];
            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(column);
            }
        }
        s.append(' ');
    }

    /**
     * addClause
     * @param s      the add StringBuilder
     * @param name   clauseName
     * @param clause clauseSelection
     */
    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    private static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * 执行查询
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return List<Map<String, Object>>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object[] bindArgs,
                                                         DatabaseParam databaseParam) throws SQLException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /**获取数据库连接池中的连接**/
            connection = CommonUtil.getConnection(databaseParam);
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            dataList = getDataList(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭连接
            CommonUtil.close(connection, null, preparedStatement, resultSet);
        }
        return dataList;
    }

    /**
     * 将结果集对象封装成List<Map<String, Object>> 对象
     * @param resultSet 结果集
     * @return 结果的封装
     * @throws SQLException
     */
    private static List<Map<String, Object>> getDataList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        /**获取结果集的数据结构对象**/
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowMap.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            dataList.add(rowMap);
        }
        return dataList;
    }

    /**
     * 可以执行新增，修改，删除
     * @param sql      sql语句
     * @param bindArgs 绑定参数
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(String sql, Object[] bindArgs,
                                    DatabaseParam databaseParam) throws SQLException {
        /**影响的行数**/
        int affectRowCount = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            /**从数据库连接池中获取数据库连接**/
            connection = CommonUtil.getConnection(databaseParam);
            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql);
            /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
            connection.setAutoCommit(false);
            if (bindArgs != null) {
                /**绑定参数设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            /**执行sql**/
            affectRowCount = preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            CommonUtil.close(connection, null, preparedStatement, null);
        }
        return affectRowCount;
    }

    /***
     * 创建表结构
     * @param sql
     * @param databaseParam
     */
    public static void createTable(String sql, DatabaseParam databaseParam) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            /**从数据库连接池中获取数据库连接**/
            connection = CommonUtil.getConnection(databaseParam);
            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql);
            /**执行创建表*/
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CommonUtil.close(connection, null, preparedStatement, null);
        }
    }

    /***
     * 删除表
     * @param tableName
     * @param databaseParam
     */
    public static void dropTable(DatabaseParam databaseParam, String tableName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet tableResultSet = null;
        try {
            /**从数据库连接池中获取数据库连接**/
            connection = CommonUtil.getConnection(databaseParam);
            String catalogName = connection.getCatalog();
            String userName = databaseParam.getUsername();
            DatabaseMetaData  metaData = connection.getMetaData();
            String dropSql = "";
            if (databaseParam.getDriverClassName().indexOf(DatabaseType.mysql.name())!= -1) {
                tableResultSet = metaData.getTables(catalogName, userName, null, new String[] { "TABLE" });
            } else if (databaseParam.getDriverClassName().indexOf(DatabaseType.oracle.name())!= -1) {
                tableName = tableName.toUpperCase();
                tableResultSet = metaData.getTables(catalogName, userName.toUpperCase(), null, new String[] { "TABLE" });
            } else if (databaseParam.getDriverClassName().indexOf(DatabaseType.sqlserver.name())!= -1) {
                tableResultSet = metaData.getTables(catalogName, null, "%", new String[] { "TABLE" });
            }
            while (tableResultSet.next()) {
                if (tableName.equalsIgnoreCase(tableResultSet.getString("TABLE_NAME"))) {
                    dropSql =  "DROP TABLE " + tableName;
                    break;
                }
            }
            if ("".equals(dropSql)) {
                return;
            }
            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(dropSql);
            /**执行创建表*/
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CommonUtil.close(connection, null, preparedStatement, tableResultSet);
        }
    }

    /**
     * 获取某张表中数据总量的sql
     * @param tableName
     * @return
     */
    public static int getTotalCount(DatabaseParam param, String tableName) {
        //计算总数量
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = CommonUtil.getConnection(param);
            String countSql = "SELECT COUNT(*) FROM " + tableName;
            preparedStatement = connection.prepareStatement(countSql);
            resultSet = preparedStatement.executeQuery();
            int totalCount = 0;
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }
            return totalCount;
        }catch (SQLException e) {
            return 0;
        }finally {
            CommonUtil.close(connection, null, preparedStatement, resultSet);
        }
    }

    /**
     * 根据数据库相关参数获取该库所有表名
     * @param databaseParam
     * @return
     * @throws SQLException
     */
    public static List<String> getTableNameList(DatabaseParam databaseParam) {
        if (databaseParam.getDriverClassName().indexOf(DatabaseType.mysql.name())!= -1) {
            //解决MySQL返回所有系统表的问题
            databaseParam.setLinkedUrl(databaseParam.getLinkedUrl()+"&nullCatalogMeansCurrent=true");
        }
        Connection connection = null;
        ResultSet tableResultSet = null;
        List<String> tableNameList = new ArrayList<>();
        try {
            connection = CommonUtil.getConnection(databaseParam);
            DatabaseMetaData metaData = connection.getMetaData();
            tableResultSet = metaData.getTables(null, null, null, new String[] { "TABLE" });
            while (tableResultSet.next()) {
                tableNameList.add(tableResultSet.getString("TABLE_NAME"));
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            CommonUtil.close(connection, null, null, tableResultSet);
        }
        return tableNameList;
    }

    /**
     * 根据数据库相关参数获取所有的表相关信息
     * @param databaseParam
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> getTableInfoList(DatabaseParam databaseParam) throws SQLException{
        if (databaseParam.getDriverClassName().indexOf(DatabaseType.mysql.name())!= -1) {
            //解决MySQL返回所有系统表的问题
            databaseParam.setLinkedUrl(databaseParam.getLinkedUrl()+"&nullCatalogMeansCurrent=true");
        }
        Connection connection = null;
        ResultSet tableResultSet = null;
        List<Map<String,String>> tableInfoList = new ArrayList<>();
        String dbName = "";
        try {
            connection = CommonUtil.getConnection(databaseParam);
            dbName = connection.getCatalog();
            DatabaseMetaData metaData = connection.getMetaData();
            tableResultSet = metaData.getTables(null, null, null, new String[] { "TABLE" });
            while (tableResultSet.next()) {
                Map<String, String> tableInfo = new HashMap<>();
                tableInfo.put("tableName",tableResultSet.getString("TABLE_NAME"));
                tableInfo.put("remarks",tableResultSet.getString("REMARKS"));
                tableInfo.put("tableType",tableResultSet.getString("TABLE_TYPE"));
                tableInfoList.add(tableInfo);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            CommonUtil.close(connection, null, null, tableResultSet);
        }
        Map<String, Object> tableInfoListMap = new HashMap<>();
        tableInfoListMap.put("dbName", dbName);
        tableInfoListMap.put("tableInfoList", tableInfoList);
        return tableInfoListMap;
    }

    /**
     * 根据表名获取表结构信息
     * @param tableName
     * @param databaseParam
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> getFieldsByTableName(String tableName,
                                                          DatabaseParam databaseParam) throws SQLException{
        if (databaseParam.getDriverClassName().indexOf(DatabaseType.mysql.name())!= -1) {
            //解决MySQL返回所有系统表的问题
            databaseParam.setLinkedUrl(databaseParam.getLinkedUrl()+"&nullCatalogMeansCurrent=true");
        }
        Connection connection = null;
        List<Map<String, Object>> fieldList = new ArrayList<>();
        try {
            connection = CommonUtil.getConnection(databaseParam);
            DatabaseMetaData  metaData = connection.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tableName, null);
            while (rs.next()) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("columnName", rs.getString("COLUMN_NAME"));
                fieldMap.put("columnType", rs.getString("TYPE_NAME"));
                fieldMap.put("columnSize", rs.getInt("COLUMN_SIZE"));
                fieldMap.put("nullable", rs.getInt("NULLABLE"));
                fieldMap.put("isAutoIncrement",rs.getString("IS_AUTOINCREMENT"));
                fieldMap.put("remarks",rs.getString("REMARKS"));
                fieldMap.put("decimalDigits", rs.getInt("DECIMAL_DIGITS"));
                fieldList.add(fieldMap);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            CommonUtil.close(connection, null, null,null);
        }
        Map<String, Object> fieldListMap = new HashMap<>();
        fieldListMap.put("tableName", tableName);
        fieldListMap.put("fieldList", fieldList);
        return fieldListMap;
    }

    /***
     * 根据数据库连接信息获取所有的表名及表中的所有字段信息
     * @param databaseParam
     * @return
     */
    public static Map<String, Object> getTableNamesAndFieldsByDataByDBName(DatabaseParam databaseParam) {
        String databaseType = ""; //数据库类型
        String userName = ""; //数据库用户名（用于oracle查询表结构信息）
        if (databaseParam.getDriverClassName().indexOf(DatabaseType.mysql.name())!= -1) {
            //解决MySQL返回所有系统表的问题
            databaseParam.setLinkedUrl(databaseParam.getLinkedUrl()+"&nullCatalogMeansCurrent=true");
            databaseType = DatabaseType.mysql.name();
            userName = databaseParam.getUsername();
        } else if (databaseParam.getDriverClassName().indexOf(DatabaseType.oracle.name())!= -1){
            databaseType = DatabaseType.oracle.name();
            userName = databaseParam.getUsername();
        } else if (databaseParam.getDriverClassName().indexOf(DatabaseType.sqlserver.name()) != -1) {
            databaseType = DatabaseType.sqlserver.name();
        }
        List<Object> tableFieldInfoList = new ArrayList<>();
        Map<String, Object> tableFieldInfoListMap = new HashMap<>();
        Connection connection = null;
        try {
            connection = CommonUtil.getConnection(databaseParam);
            DatabaseMetaData  metaData = connection.getMetaData();
            String catalogName = connection.getCatalog();
            //根据数据库名称获取所有的表名称
            ResultSet tableResultSet = null;
            if (DatabaseType.mysql.name().equals(databaseType)) {
                tableResultSet = metaData.getTables(catalogName, userName, null, new String[] { "TABLE" });
            } else if (DatabaseType.oracle.name().equals(databaseType)) {
                tableResultSet = metaData.getTables(catalogName, userName.toUpperCase(), null, new String[] { "TABLE" });
            } else if (DatabaseType.sqlserver.name().equals(databaseType)) {
                tableResultSet = metaData.getTables(catalogName, null, "%", new String[] { "TABLE" });
            }
            List<String> tableNames = new ArrayList<>();
            while (tableResultSet.next()) {
                tableNames.add(tableResultSet.getString("TABLE_NAME"));
            }
            if (tableNames != null && !tableNames.isEmpty()) {
                //遍历tableNames
                for (int i=0; i<tableNames.size(); i++) {
                    ResultSet fieldResultSet = metaData.getColumns(null, null, tableNames.get(i), null);
                    List<Map<String, Object>> fieldInfoList = new ArrayList<>();
                    Map<String, Object> tableFieldInfoMap = new HashMap<>();
                    ResultSet pkResult = metaData.getPrimaryKeys(null, null, tableNames.get(i));
                    while (pkResult.next()) {
                        //获取主键
                        tableFieldInfoMap.put("primaryKey", pkResult.getString("COLUMN_NAME"));
                    }
                    while (fieldResultSet.next()) {
                        Map<String, Object> fieldMap = new LinkedHashMap<>();
                        fieldMap.put("columnName", fieldResultSet.getString("COLUMN_NAME"));
                        fieldMap.put("columnType", fieldResultSet.getString("TYPE_NAME"));
                        fieldMap.put("columnSize", fieldResultSet.getInt("COLUMN_SIZE"));
                        fieldMap.put("nullable", fieldResultSet.getInt("NULLABLE"));
                        //mysql和oracle有IS_AUTOINCREMENT属性
                        if (DatabaseType.mysql.name().equals(databaseType) || DatabaseType.oracle.name().equals(databaseType)) {
                            fieldMap.put("isAutoIncrement",fieldResultSet.getString("IS_AUTOINCREMENT"));
                        }else if(DatabaseType.sqlserver.name().equals(databaseType)){//sqlserver通过identity判断是否是递增
                            if (fieldResultSet.getString("TYPE_NAME").indexOf("identity") != -1)
                                fieldMap.put("isAutoIncrement","YES");
                            else fieldMap.put("isAutoIncrement","NO");
                        }
                        fieldMap.put("remarks",fieldResultSet.getString("REMARKS"));
                        fieldMap.put("decimalDigits", fieldResultSet.getInt("DECIMAL_DIGITS"));
                        fieldInfoList.add(fieldMap);
                    }
                    tableFieldInfoMap.put("tableName", tableNames.get(i));
                    tableFieldInfoMap.put("fieldInfoList", fieldInfoList);
                    tableFieldInfoList.add(tableFieldInfoMap);
                }
                tableFieldInfoListMap.put("dbName", catalogName);
                tableFieldInfoListMap.put("tableFieldInfoList",tableFieldInfoList);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            CommonUtil.close(connection, null, null,null);
        }
        return tableFieldInfoListMap;
    }

    /**
     * 测试数据库是否连通
     * @param databaseParam
     * @return
     */
    public static boolean isConnected(DatabaseParam databaseParam) {
        Connection connection = null;
        try {
            connection = CommonUtil.getConnection(databaseParam);
            if (connection == null) {
                return false;
            }
            return true;
        }catch (Exception e) {
            return false;
        }finally {
            CommonUtil.close(connection, null, null,null);
        }
    }

    /**
     *  oracle字段添加注释（创建表时无法添加注释，需要另外单独添加）
     * @param commentMap
     * @param param
     * @param tableName
     */
    public static void addFieldCommentsForOracle(Map<String,String> commentMap,
                                                 DatabaseParam param, String tableName) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = CommonUtil.getConnection(param); //输出数据库连接
            statement = connection.createStatement();
            for (Map.Entry<String, String> entry : commentMap.entrySet()) {
                statement.addBatch("COMMENT ON COLUMN " + tableName + "." + entry.getKey()
                        + " IS " + "'"+entry.getValue()+"'");
            }
            statement.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            CommonUtil.close(connection, statement, null,null);
        }
    }

    /**
     * sqlServer字段添加注释（创建表时无法添加注释，需要另外单独添加）
     * @param commentMap
     * @param param
     * @param tableName
     */
    public static void addFieldCommentsForSqlServer(Map<String,String> commentMap,
                                                    DatabaseParam param, String tableName) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = CommonUtil.getConnection(param); //输出数据库连接
            statement = connection.createStatement();
            for (Map.Entry<String, String> entry : commentMap.entrySet()) {
                statement.addBatch("EXECUTE sp_addextendedproperty N'MS_Description'," + entry.getValue()
                +", N'user', N'dbo', N'table',"+ "N'"+tableName+"', N'column', N'" + entry.getKey()+"'");
            }
            statement.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            CommonUtil.close(connection, statement, null,null);
        }
    }

    /***
     * 通过fromSql和fromParam查询到输入数据表中所有数据
     * 通过toParam连接输出数据源，插入到输出数据表中
     */
    public static void batchInsertToAnotherTable(DatabaseParam importParam, DatabaseParam exportParam,
                                                 String importTableName, String importSelectSql, int pageSize) {
        //输入数据源
        Connection importConnection = null;
        PreparedStatement importPreparedStatement = null;
        ResultSet importResultSet = null;
        //输出数据源
        Connection exportConnection = null;
        PreparedStatement exportPreparedStatement = null;
        //如果是mysql，需要mysql数据库支持批处理
        exportParam = CommonUtil.getHandledDatabaseParam(exportParam);
        try {
            int totalCount = getTotalCount(importParam, importTableName);
            int pageCount = CommonUtil.getPageCount(totalCount);
            StringBuilder insertSqlBuilder = new StringBuilder();
            for (int t=0,pageCountLen=pageCount; t<=pageCountLen; t++) {
                importConnection = CommonUtil.getConnection(importParam);
                try {
                    //分页获取数据
                    String pageImportSelectSql = CommonUtil.getPaginationSql(importParam, importSelectSql, t, pageSize);
                    importPreparedStatement = importConnection.prepareStatement(pageImportSelectSql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    importResultSet = importPreparedStatement.executeQuery();
                    int size = importResultSet.getMetaData().getColumnCount();//结果集获取到的长度
                    if (t == 0) { //只在第一次组织insertSql语句
                        insertSqlBuilder = CommonUtil.getInsertSql(insertSqlBuilder, importTableName, size);
                    }
                    exportConnection = CommonUtil.getConnection(exportParam); //输出数据库连接
                    exportPreparedStatement = exportConnection.prepareStatement(insertSqlBuilder.toString());
                    int count = 0;
                    exportConnection.setAutoCommit(false);
                    while (importResultSet.next()) {
                        ++count;
                        for (int i=1,len=size; i<=len; i++) {
                            exportPreparedStatement.setObject(i, importResultSet.getObject(i));
                        }
                        exportPreparedStatement.addBatch();//将预先语句存储起来，这里还没有向数据库插入
                        if (count % 1000 == 0) {//每1000条向数据库提交
                            exportPreparedStatement.executeBatch();
                            exportConnection.commit();
                            exportPreparedStatement.clearBatch(); //清空积攒的sql
                        }
                    }
                    exportPreparedStatement.executeBatch();//执行剩下不足1000条数据
                    exportConnection.commit(); //提交
                }finally {
                    CommonUtil.close(importConnection, null, importPreparedStatement, importResultSet);
                    CommonUtil.close(exportConnection, null, exportPreparedStatement, null);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            try {
                exportConnection.rollback();//回滚
            }catch (SQLException se) {}
        }
    }


}
