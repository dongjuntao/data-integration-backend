package com.softline.database.common;

import com.alibaba.fastjson.JSONObject;
import com.softline.database.enums.DatabaseType;

/**
 * 数据库库直接数据类型转换
 * Created by dong ON 2020/12/28
 */
public class FieldTypeConvert {

    /**
     * 数据类型转换
     * @param fromDatabaseType
     * @param toDatabaseType
     * @param tableInfo
     * @return
     */
    public static String getConvertedFieldType(String fromDatabaseType, String toDatabaseType, JSONObject tableInfo) {
        if (DatabaseType.mysql.name().equalsIgnoreCase(fromDatabaseType)) {
            if (DatabaseType.oracle.name().equalsIgnoreCase(toDatabaseType)) { //mysql转oracle
                return getOracleFieldTypeByMysqlField(tableInfo);
            } else if (DatabaseType.sqlserver.name().equalsIgnoreCase(toDatabaseType)) { //mysql转sqlServer
                return getSqlServerFieldTypeByMysqlField(tableInfo);
            }
        } else if (DatabaseType.oracle.name().equalsIgnoreCase(fromDatabaseType)) {
            if (DatabaseType.mysql.name().equalsIgnoreCase(toDatabaseType)) { //oracle转mysql
                return getMysqlFieldTypeByOracleField(tableInfo);
            } else if (DatabaseType.sqlserver.name().equalsIgnoreCase(toDatabaseType)) { //oracle转sqlServer
                return getSqlServerFieldTypeByOracleField(tableInfo);
            }
        } else if (DatabaseType.sqlserver.name().equalsIgnoreCase(fromDatabaseType)) {
            if (DatabaseType.mysql.name().equalsIgnoreCase(toDatabaseType)) { //sqlServer转mysql
                return getMysqlFieldTypeBySqlServerField(tableInfo);
            } else if (DatabaseType.oracle.name().equalsIgnoreCase(toDatabaseType)) { //sqlServer转oracle
                return getOracleFieldTypeBySqlServerField(tableInfo);
            }
        }
        return null;
    }

    /**
     * mysql, oracle, sqlserver获取数据类型和数据长度
     * @param databaseType
     * @param columnType
     * @param size
     * @param decimalDigits
     * @return
     */
    public static String getFieldTypeAndSize(String databaseType, String columnType, int size, int decimalDigits) {
        if (DatabaseType.mysql.name().equalsIgnoreCase(databaseType)) {
            return getMysqlFieldTypeAndSize(columnType, size, decimalDigits);
        } else if (DatabaseType.oracle.name().equalsIgnoreCase(databaseType)) {
            return getOracleFieldTypeAndSize(columnType, size, decimalDigits);
        } else if (DatabaseType.sqlserver.name().equalsIgnoreCase(databaseType)) {
            return getSqlServerFieldTypeAndSize(columnType, size, decimalDigits);
        }
        return null;
    }

    /**
     * mysql获取数据类类型和长度
     * @param columnType
     * @param columnSize
     * @param decimalDigits
     * @return
     */
    private static String getMysqlFieldTypeAndSize(String columnType, int columnSize, int decimalDigits) {
        String fieldTypeAndSize = columnType;
        switch (fieldTypeAndSize.toUpperCase()) {
            case "BIGINT" : fieldTypeAndSize = "BIGINT";break;
            case "BINARY" : fieldTypeAndSize = "BINARY("+columnSize+")"; break;
            case "BIT" : fieldTypeAndSize = "BIT("+columnSize+")"; break;
            case "BLOB" : fieldTypeAndSize = "BLOB"; break;
            case "CHAR" : fieldTypeAndSize = "CHAR("+columnSize+")"; break;
            case "DATE" : fieldTypeAndSize = "DATE"; break;
            case "DATETIME" : fieldTypeAndSize = "DATETIME"; break;
            case "DECIMAL" : fieldTypeAndSize = "DECIMAL("+columnSize+","+decimalDigits+")"; break;
            case "DOUBLE" : fieldTypeAndSize = "DOUBLE("+columnSize+","+decimalDigits+")"; break;
            case "FLOAT": fieldTypeAndSize = "FLOAT("+columnSize+","+decimalDigits+")"; break;
            case "GEOMETRY" : fieldTypeAndSize = "GEOMETRY"; break;
            case "GEOMCOLLECTION" : fieldTypeAndSize = "GEOMCOLLECTION"; break;
            case "INT": fieldTypeAndSize = "INT";break;
            case "JSON" : fieldTypeAndSize = "JSON"; break;
            case "LINESTRING" : fieldTypeAndSize = "LINESTRING"; break;
            case "LONGBLOB" : fieldTypeAndSize = "LONGBLOB"; break;
            case "LONGTEXT": fieldTypeAndSize = "LONGTEXT"; break;
            case "MEDIUMBLOB" : fieldTypeAndSize = "MEDIUMBLOB"; break;
            case "MEDIUMINT": fieldTypeAndSize = "MEDIUMINT";break;
            case "MEDIUMTEXT": fieldTypeAndSize = "MEDIUMTEXT"; break;
            case "MULTILINESTRING" : fieldTypeAndSize = "MULTILINESTRING"; break;
            case "MULTIPOINT" : fieldTypeAndSize = "MULTIPOINT"; break;
            case "MULTIPOLYGON" : fieldTypeAndSize = "MULTIPOLYGON"; break;
            case "POINT" : fieldTypeAndSize = "POINT"; break;
            case "POLYGON" : fieldTypeAndSize = "POLYGON"; break;
            case "SMALLINT": fieldTypeAndSize = "SMALLINT";break;
            case "TEXT": fieldTypeAndSize = "TEXT"; break;
            case "TIME": fieldTypeAndSize = "TIME"; break;
            case "TIMESTAMP" : fieldTypeAndSize = "TIMESTAMP"; break;
            case "TINYBLOB": fieldTypeAndSize = "TINYBLOB"; break;
            case "TINYINT": fieldTypeAndSize = "TINYINT";break;
            case "TINYTEXT": fieldTypeAndSize = "TINYTEXT"; break;
            case "VARBINARY": fieldTypeAndSize = "VARBINARY("+columnSize+")"; break;
            case "VARCHAR" : fieldTypeAndSize = "VARCHAR("+columnSize+")"; break;
            case "YEAR": fieldTypeAndSize = "YEAR"; break;
        }
        return fieldTypeAndSize;
    }

    /**
     * oracle获取数据类类型和长度
     * @param columnType
     * @param columnSize
     * @param decimalDigits
     * @return
     */
    private static String getOracleFieldTypeAndSize(String columnType, int columnSize, int decimalDigits) {
        String fieldTypeAndSize = columnType;
        switch (fieldTypeAndSize.toUpperCase()) {
            case "BFILE" : fieldTypeAndSize = "BFILE"; break;
            case "BINARY_DOUBLE" : fieldTypeAndSize = "BINARY_DOUBLE"; break;
            case "BINARY_FLOAT" : fieldTypeAndSize = "BINARY_FLOAT"; break;
            case "BLOB" : fieldTypeAndSize = "BLOB"; break;
            case "CHAR":
            case "CHARACTER" :
                fieldTypeAndSize = "CHAR("+columnSize+")"; break;
            case "VARCHAR2":
            case "CHAR VARYING" :
            case "CHARACTER VARYING" :
            case "VARCHAR" :
                fieldTypeAndSize = "VARCHAR2("+columnSize+")"; break;
            case "CLOB" : fieldTypeAndSize = "CLOB"; break;
            case "DATE" : fieldTypeAndSize = "DATE"; break;
            case "NUMBER":
            case "DECIMAL":
            case "NUMERIC" :
                fieldTypeAndSize = "NUMBER("+columnSize+","+decimalDigits+")"; break;
            case "FLOAT" : fieldTypeAndSize = "FLOAT("+columnSize+")"; break;
            case "INTERVAL DAY TO SECOND" : fieldTypeAndSize = "INTERVAL DAY TO SECOND"; break;
            case "INTERVAL YEAR TO MONTH" : fieldTypeAndSize = "INTERVAL YEAR TO MONTH"; break;
            case "LONG" : fieldTypeAndSize = "LONG"; break;
            case "NCHAR":
            case "NATIONAL CHAR" :
            case "NATIONAL CHARACTER" :
                fieldTypeAndSize = "NCHAR("+columnSize+")"; break;
            case "NVARCHAR2":
            case "NATIONAL CHAR VARYING" :
            case "NATIONAL CHARACTER VARYING" :
            case "NCHAR VARYING" :
                fieldTypeAndSize = "NVARCHAR2("+columnSize+")"; break;
            case "NCLOB " : fieldTypeAndSize = "NCLOB "; break;
            case "RAW" : fieldTypeAndSize = "RAW("+columnSize+")"; break;
            case "LONG RAW" : fieldTypeAndSize = "LONG RAW"; break;
            case "LONG VARCHAR" : fieldTypeAndSize = "LONG VARCHAR"; break;
            case "ROWID" : fieldTypeAndSize = "ROWID"; break;
            case "TIMESTAMP": fieldTypeAndSize = "TIMESTAMP"; break;
            case "TIMESTAMP WITH LOCAL TIME ZONE": fieldTypeAndSize = "TIMESTAMP WITH LOCAL TIME ZONE"; break;
            case "TIMESTAMP WITH TIME ZONE" : fieldTypeAndSize = "TIMESTAMP WITH TIME ZONE"; break;
            case "UROWID" : fieldTypeAndSize = "UROWID("+columnSize+")"; break;
            case "DOUBLE PRECISION" : fieldTypeAndSize = "FLOAT(126)"; break;
            case "INT" :
            case "INTEGER" :
            case "SMALLINT" :
                fieldTypeAndSize = "NUMBER"; break;
            case "REAL" : fieldTypeAndSize = "FLOAT(63)"; break;
        }
        return fieldTypeAndSize;
    }

    /**
     * sqlserver获取数据类类型和长度
     * @param columnType
     * @param columnSize
     * @param decimalDigits
     * @return
     */
    private static String getSqlServerFieldTypeAndSize(String columnType, int columnSize, int decimalDigits) {
        String fieldTypeAndSize = columnType;
        switch (fieldTypeAndSize.toUpperCase()) {
            case "BIGINT" : fieldTypeAndSize = "BIGINT"; break;
            case "BINARY" : fieldTypeAndSize = "BINARY("+columnSize+")"; break;
            case "BIT" : fieldTypeAndSize = "BIT"; break;
            case "CHAR" : fieldTypeAndSize = "CHAR("+columnSize+")"; break;
            case "DATE" : fieldTypeAndSize = "DATE"; break;
            case "DATETIME" : fieldTypeAndSize = "DATETIME"; break;
            case "DATETIME2" : fieldTypeAndSize = "DATETIME2"; break;
            case "DATETIMEOFFSET" : fieldTypeAndSize = "DATETIMEOFFSET"; break;
            case "DECIMAL" : fieldTypeAndSize = "DECIMAL("+columnSize+","+decimalDigits+")"; break;
            case "FLOAT" : fieldTypeAndSize = "FLOAT("+columnSize+")"; break;
            case "GEOGRAPHY" : fieldTypeAndSize = "GEOGRAPHY"; break;
            case "GEOMETRY" : fieldTypeAndSize = "GEOMETRY"; break;
            case "HIERARCHYID" : fieldTypeAndSize = "HIERARCHYID"; break;
            case "IMAGE" : fieldTypeAndSize = "IMAGE"; break;
            case "INT" : fieldTypeAndSize = "INT"; break;
            case "MONEY" : fieldTypeAndSize = "MONEY"; break;
            case "NCHAR" : fieldTypeAndSize = "NCHAR("+columnSize+")"; break;
            case "NTEXT" : fieldTypeAndSize = "NTEXT"; break;
            case "NUMERIC" : fieldTypeAndSize = "NUMERIC("+columnSize+","+decimalDigits+")"; break;
            case "NVARCHAR" : fieldTypeAndSize = "NVARCHAR("+columnSize+")"; break;
            case "VARCHAR(MAX)" : fieldTypeAndSize = "VARCHAR(MAX)"; break;
            case "REAL" : fieldTypeAndSize = "REAL"; break;
            case "SMALLDATETIME" : fieldTypeAndSize = "SMALLDATETIME"; break;
            case "SMALLINT" : fieldTypeAndSize = "SMALLINT"; break;
            case "SMALLMONEY" : fieldTypeAndSize = "SMALLMONEY"; break;
            case "SQL_VARIANT" : fieldTypeAndSize = "SQL_VARIANT"; break;
            case "SYSNAME" : fieldTypeAndSize = "SYSNAME"; break;
            case "TEXT" : fieldTypeAndSize = "TEXT"; break;
            case "TIME" : fieldTypeAndSize = "TIME"; break;
            case "TIMESTAMP" : fieldTypeAndSize = "TIMESTAMP"; break;
            case "TINYINT" : fieldTypeAndSize = "TINYINT"; break;
            case "UNIQUEIDENTIFIER" : fieldTypeAndSize = "UNIQUEIDENTIFIER"; break;
            case "VARBINARY" : fieldTypeAndSize = "VARBINARY("+columnSize+")"; break;
            case "VARBINARY(MAX)" : fieldTypeAndSize = "VARBINARY(MAX)"; break;
            case "VARCHAR" : fieldTypeAndSize = "VARCHAR("+columnSize+")"; break;
            case "NVARCHAR(MAX)" : fieldTypeAndSize = "NVARCHAR(MAX)"; break;
            case "XML" : fieldTypeAndSize = "XML"; break;
            //带有自增的类型
            case "INT IDENTITY" : fieldTypeAndSize = "INT IDENTITY"; break;
            case "SMALLINT IDENTITY" : fieldTypeAndSize = "SMALLINT IDENTITY"; break;
            case "TINYINT IDENTITY" : fieldTypeAndSize = "TINYINT IDENTITY"; break;
            case "BIGINT IDENTITY" : fieldTypeAndSize = "BIGINT IDENTITY"; break;
        }
        return fieldTypeAndSize;
    }
    /**
     * mysql数据类型转oracle数据类型（适用于mysql转oracle数据库）
     * @param tableInfo
     * @return
     */
    private static String getOracleFieldTypeByMysqlField(JSONObject tableInfo) {
        String oracleFieldType = tableInfo.getString("columnType"); //字段类型
        int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
        int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
        switch (oracleFieldType.toUpperCase()) {
            case "BIGINT": oracleFieldType = "NUMBER(20,0)";break;//Mysql中BIGINT相当于oracle中NUMBER(20,0),固定的
            case "BINARY" :
            case "BLOB" :
            case "LONGBLOB" :
            case "MEDIUMBLOB" :
            case "TINYBLOB" :
            case "VARBINARY" :
                oracleFieldType = "BLOB"; break;
            case "BIT": oracleFieldType = "VARCHAR2("+columnSize+")"; break;
            case "CHAR" : oracleFieldType = "NCHAR("+columnSize+")"; break;
            case "DATE" :
            case "DATETIME" :
            case "TIMESTAMP" :
                oracleFieldType = "DATE"; break;
            case "DECIMAL" :
            case "DOUBLE" :
            case "FLOAT":
                oracleFieldType = "NUMBER("+columnSize+","+decimalDigits+")"; break;
            case "ENUM" :
            case "VARCHAR" :
                oracleFieldType = "NVARCHAR2("+columnSize+")"; break;
            case "GEOMETRY" :
            case "GEOMCOLLECTION" :
            case "JSON" :
            case "LINESTRING" :
            case "MULTILINESTRING" :
            case "MULTIPOINT" :
            case "MULTIPOLYGON" :
            case "POINT" :
            case "POLYGON" :
                oracleFieldType = "CLOB"; break;
            case "INT" : oracleFieldType = "NUMBER(11)"; break;
            case "LONGTEXT" :
            case "MEDIUMTEXT" :
            case "TEXT" :
            case "TINYTEXT" :
                oracleFieldType = "NCLOB"; break;
            case "MEDIUMINT" : oracleFieldType = "NUMBER(9,0)"; break;
            case "SET" : oracleFieldType = "NVARCHAR2(255)"; break;
            case "SMALLINT" : oracleFieldType = "NUMBER(6,0)"; break;
            case "TIME" : oracleFieldType = "VARCHAR2(255)"; break;
            case "TINYINT" : oracleFieldType = "NUMBER(4,0)"; break;
            case "YEAR" : oracleFieldType = "CHAR(4,0)"; break;
        }
        return oracleFieldType;
    }

    /**
     * oracle数据类型转mysql数据类型（适用于oracle转mysql数据库）
     * @param tableInfo
     * @return
     */
    private static String getMysqlFieldTypeByOracleField(JSONObject tableInfo) {
        String mysqlFieldType = tableInfo.getString("columnType");
        int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
        int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
        switch (mysqlFieldType.toUpperCase()) {
            case "BFILE" : mysqlFieldType = "TEXT"; break;
            case "BINARY_DOUBLE" :
            case "FLOAT" :
                mysqlFieldType = "DOUBLE"; break;
            case "BINARY_FLOAT" : mysqlFieldType = "FLOAT"; break;
            case "BLOB" :
            case "RAW" :
                mysqlFieldType = "LONGBLOB"; break;
            case "CHAR" :
            case "NCHAR" :
                mysqlFieldType = "CHAR("+columnSize+")"; break;
            case "VARCHAR2" :
                if (columnSize <= 2000) mysqlFieldType = "VARCHAR("+columnSize+")";
                else if (columnSize > 2000) mysqlFieldType = "TEXT";
                break;
            case "CLOB":
            case "INTERVAL DAY TO SECOND" :
            case "INTERVAL YEAR TO MONTH" :
            case "LONG" :
            case "LONG RAW" :
            case "LONG VARCHAR" :
            case "NCLOB" :
                mysqlFieldType = "LONGTEXT"; break;
            case "DATE" :
            case "TIMETAMP" :
            case "TIMESTSAMP WITH LOCAL TIME ZONE" :
            case "TIMESTAMP WITH TIME ZONE" :
                mysqlFieldType = "DATETIME"; break;
            case "NUMBER" :
                if (columnSize == 0) mysqlFieldType = "DECIMAL(65,0)";
                else mysqlFieldType = "DECIMAL("+columnSize+","+decimalDigits+")";
                break;
            case "NVARCHAR2" : mysqlFieldType = "VARCHAR("+columnSize+")"; break;
            case "ROWID" : mysqlFieldType = "VARCHAR(10)"; break;
            case "UROWID" : mysqlFieldType = "VARCHAR(255)"; break;
        }
        return mysqlFieldType;
    }

    /**
     * sqlServer数据类型转mysql数据类型（适用于sqlServer转mysql数据库）
     * @param tableInfo
     * @return
     */
    private static String getMysqlFieldTypeBySqlServerField(JSONObject tableInfo) {
        String mysqlFieldType = tableInfo.getString("columnType");
        int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
        int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
        switch (mysqlFieldType.toUpperCase()) {
            case "BIGINT" : mysqlFieldType = "BIGINT"; break;
            case "BINARY" :
            case "IMAGE" :
            case "SQL_VARIANT " :
            case "TIMESTAMP" :
            case "VARBINARY" :
            case "VARBINARY(MAX)" :
            case "GEOGRAPHY" :
            case "GEOMETRY" :
            case "HIERARCHYID" :
                mysqlFieldType = "LONGBLOB"; break;
            case "BIT" :
            case "TINYINT" :
                mysqlFieldType = "TINYINT"; break;
            case "CHAR" :
            case "NCHAR" :
                mysqlFieldType = "CHAR("+columnSize+")"; break;
            case "DATE" : mysqlFieldType = "DATE"; break;
            case "DATETIME" :
            case "DATETIME2" :
            case "DATETIMEOFFSET" :
            case "SMALLDATETIME" :
                mysqlFieldType = "DATETIME"; break;
            case "DECIMAL" :
            case "NUMERIC" :
                mysqlFieldType = "DECIMAL("+columnSize+","+decimalDigits+")"; break;
            case "FLOAT" : mysqlFieldType = "DOUBLE"; break;
            case "INT" : mysqlFieldType = "INT"; break;
            case "MONEY" : mysqlFieldType = "DECIMAL(19,4)"; break;
            case "NTEXT" :
            case "NVARCHAR(MAX)" :
            case "TEXT" :
            case "VARCHAR(MAX)" :
            case "XML" :
                mysqlFieldType = "LONGTEXT"; break;
            case "NVARCHAR" :
            case "VARCHAR" :
                mysqlFieldType = "VARCHAR("+columnSize+")"; break;
            case "REAL" : mysqlFieldType = "FLOAT"; break;
            case "SMALLINT" : mysqlFieldType = "SMALLINT"; break;
            case "SMALLMONEY" : mysqlFieldType  = "DECIMAL(10,4)"; break;
            case "SYSNAME" : mysqlFieldType = "VARCHAR(128)"; break;
            case "TIME" : mysqlFieldType = "TIME"; break;
            case "UNIQUEIDENTIFIER" : mysqlFieldType = "CHAR(36)"; break;
        }
        return mysqlFieldType;
    }


    /**
     * mysql数据类型转sqlServer数据类型（适用于mysql转sqlServer数据库）
     * @param tableInfo
     * @return
     */
    private static String getSqlServerFieldTypeByMysqlField(JSONObject tableInfo) {
        String sqlServerFieldType = tableInfo.getString("columnType");
        int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
        int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
        switch (sqlServerFieldType.toUpperCase()) {
            case "BIGINT": sqlServerFieldType = "BIGINT";break;
            case "BINARY" :
            case "BLOB" :
            case "LONGBLOB" :
            case "MEDIUMBLOB" :
            case "TINYBLOB" :
            case "VARBINARY" :
                sqlServerFieldType = "VARBINARY(MAX)"; break;
            case "BIT": sqlServerFieldType = "VARCHAR("+columnSize+")"; break;
            case "CHAR" : sqlServerFieldType = "NCHAR("+columnSize+")"; break;
            case "DATE" : sqlServerFieldType = "DATE"; break;
            case "DATETIME" :
            case "TIMESTAMP" :
                sqlServerFieldType = "DATETIME"; break;
            case "DECIMAL" : sqlServerFieldType = "DECIMAL("+columnSize+","+decimalDigits+")"; break;
            case "DOUBLE" :
            case "REAL" :
                sqlServerFieldType = "FLOAT(53)"; break;
            case "ENUM" :
            case "SET" :
                sqlServerFieldType = "NVARCHAR(255)"; break;
            case "FLOAT" : sqlServerFieldType = "REAL"; break;
            case "GEOMETRY" :
            case "GEOMCOLLECTION" :
            case "JSON" :
            case "LINESTRING" :
            case "MULTILINESTRING" :
            case "MULTIPOINT" :
            case "MULTIPOLYGON" :
            case "POINT" :
            case "POLYGON" :
                sqlServerFieldType = "VARCHAR(MAX)"; break;
            case "INT" :
            case "MEDIUMINT" :
                sqlServerFieldType = "INT"; break;
            case "LONGTEXT" :
            case "MEDIUMTEXT" :
            case "TEXT" :
            case "TINYTEXT":
                sqlServerFieldType = "NVARCHAR(MAX)"; break;
            case "SMALLINT" : sqlServerFieldType = "SMALLINT"; break;
            case "TIME" : sqlServerFieldType = "TIME"; break;
            case "TINYINT" : sqlServerFieldType = "TINYINT"; break;
            case "VARCHAR" : sqlServerFieldType = "NVARCHAR("+columnSize+")"; break; //TODO
            case "YEAR": sqlServerFieldType = "CHAR(4)"; break;
        }
        return sqlServerFieldType;
    }

    /**
     * oracle数据类型转sqlServer数据类型（适用于oracle转sqlServer数据库）
     * @param tableInfo
     * @return
     */
    private static String getSqlServerFieldTypeByOracleField(JSONObject tableInfo) {
        String sqlServerFieldType = tableInfo.getString("columnType");
        int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
        int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
        switch (sqlServerFieldType.toUpperCase()) {
            case "BFILE" :
            case "VARCHAR2" :
                sqlServerFieldType = "VARCHAR("+columnSize+")"; break;
            case "BINARY_DOUBLE" :
            case "FLOAT" :
                sqlServerFieldType = "FLOAT(53)"; break;
            case "BINARY_FLOAT" : sqlServerFieldType = "REAL"; break;
            case "BLOB" :
            case "RAW" :
                sqlServerFieldType = "VARBINARY(MAX)"; break;
            case "CHAR" : sqlServerFieldType = "CHAR("+columnSize+")"; break;
            case "CLOB" :
            case "INTERVAL DAY TO SECOND" :
            case "INTERVAL YEAR TO MONTH" :
            case "LONG" :
            case "UROWID" :
                sqlServerFieldType = "VARCHAR(MAX)"; break;
            case "DATE" :
            case "TIMESTAMP" :
            case "TIMESTAMP WITH LOCAL TIME ZONE" :
            case "TIMESTAMP WITH TIME ZONE" :
                sqlServerFieldType = "DATETIME"; break;
            case "NUMBER" :
                if (columnSize == 0) sqlServerFieldType = "DECIMAL(38,0)";
                else sqlServerFieldType = "DECIMAL("+columnSize+","+decimalDigits+")";
                break;
            case "NCHAR" : sqlServerFieldType = "NCHAR("+columnSize+")"; break;
            case "NVARCHAR2" : sqlServerFieldType = "NVARCHAR("+columnSize+")"; break;
            case "NCLOB" : sqlServerFieldType = "NVARCHAR(MAX)"; break;
            case "ROWID" : sqlServerFieldType = "VARCHAR(10)"; break;
        }
        return sqlServerFieldType;
    }

    /**
     * sqlServer数据类型转oracle数据类型（适用于sqlServer转oracle数据库）
     * @param tableInfo
     * @return
     */
    private static String getOracleFieldTypeBySqlServerField(JSONObject tableInfo) {
        String oracleFieldType = tableInfo.getString("columnType");
        int columnSize = Integer.parseInt(tableInfo.getString("columnSize"));
        int decimalDigits = Integer.parseInt(tableInfo.getString("decimalDigits"));
        switch (oracleFieldType.toUpperCase()) {
            case "BIGINT" : oracleFieldType = "NUMBER(20)"; break;
            case "BINARY" :
            case "IMAGE" :
            case "SQL_VARIANT" :
            case "TEXT" :
            case "TIMESTAMP" :
            case "VARBINARY" :
            case "VARBINARY(MAX)":
                oracleFieldType = "BLOB"; break;
            case "BIT" :
            case "TINYINT" :
                oracleFieldType = "NUMBER(4,0)"; break;
            case "CHAR" : oracleFieldType = "CHAR("+columnSize+")"; break;
            case "DATE" :
            case "DATETIME" :
            case "DATETIME2" :
            case "DATETIMEOFFSET" :
            case "SMALLDATETIME" :
                oracleFieldType = "DATE"; break;
            case "DECIMAL" : oracleFieldType = "NUMBER("+columnSize+","+decimalDigits+")"; break;
            case "FLOAT" : oracleFieldType = "NUMBER(38)"; break;
            case "INT" : oracleFieldType = "NUMBER(11)"; break;
            case "MONEY" :
            case "NUMERIC" :
            case "REAL" :
            case "SMALLMONEY" :
                oracleFieldType = "NUMBER"; break;
            case "NCHAR" : oracleFieldType = "NCHAR("+columnSize+")"; break;
            case "NTEXT" :
            case "NVARCHAR(MAX)" :
            case "XML" :
                oracleFieldType = "NCLOB"; break;
            case "NVARCHAR" :
                if (columnSize <= 2000) oracleFieldType = "VARCHAR2("+columnSize+")";
                else oracleFieldType = "NCLOB";
                break;
            case "SMALLINT" : oracleFieldType = "NUMBER(6,0)"; break;
            case "SYSNAME" : oracleFieldType = "NVARCHAR2(128)"; break;
            case "TIME" : oracleFieldType = "VARCHAR2(255)"; break;
            case "UNIQUEIDENTIFIER" : oracleFieldType = "CHAR(36)"; break;
            case "VARCHAR" : oracleFieldType = "VARCHAR2("+columnSize+")"; break;
            case "VARCHAR(MAX)" : oracleFieldType = "CLOB"; break;
        }
        return oracleFieldType;
    }
}
