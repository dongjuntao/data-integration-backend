package com.softline.database.common;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.softline.common.exception.ApiException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dong ON 2020/11/30
 */
public class DataSourceFactory {

    public static ConcurrentHashMap<String, ComboPooledDataSource> dataSourceMap = new ConcurrentHashMap<>();

    public DataSourceFactory() {}

    private static synchronized ComboPooledDataSource getDataSource(final DatabaseParam databaseParam) {
        ComboPooledDataSource dataSource = null;
        try {
            String driverClassName = databaseParam.getDriverClassName();
            String url = databaseParam.getLinkedUrl();
            String username = databaseParam.getUsername();
            String password = databaseParam.getPassword();
            /**数据库连接池对象**/
            dataSource = new ComboPooledDataSource();
            /**设置数据库连接驱动**/
            dataSource.setDriverClass(driverClassName);
            /**设置数据库连接地址**/
            dataSource.setJdbcUrl(url);
            /**设置数据库连接用户名**/
            dataSource.setUser(username);
            /**设置数据库连接密码**/
            dataSource.setPassword(password);
            /**初始化时创建的连接数,应在minPoolSize与maxPoolSize之间取值.默认为3**/
            dataSource.setInitialPoolSize(3);
            /**连接池中保留的最大连接数据.默认为15**/
            dataSource.setMaxPoolSize(20);
            /**当连接池中的连接用完时，C3PO一次性创建新的连接数目;**/
            dataSource.setAcquireIncrement(1);
            /**隔多少秒检查所有连接池中的空闲连接,默认为0表示不检查;**/
            dataSource.setIdleConnectionTestPeriod(60);
            /**最大空闲时间,超过空闲时间的连接将被丢弃.为0或负数据则永不丢弃.默认为0;**/
            dataSource.setMaxIdleTime(3000);
            /**因性能消耗大请只在需要的时候使用它。如果设为true那么在每个connection提交的
             时候都将校验其有效性。建议使用idleConnectionTestPeriod或automaticTestTable
             等方法来提升连接测试的性能。Default: false**/
            dataSource.setTestConnectionOnCheckout(true);
            /**如果设为true那么在取得连接的同时将校验连接的有效性。Default: false **/
            dataSource.setTestConnectionOnCheckin(true);
            /**定义在从数据库获取新的连接失败后重复尝试获取的次数，默认为30;**/
            dataSource.setAcquireRetryAttempts(30);
            /**两次连接中间隔时间默认为1000毫秒**/
            dataSource.setAcquireRetryDelay(1000);
            /** 获取连接失败将会引起所有等待获取连接的线程异常,
             但是数据源仍有效的保留,并在下次调用getConnection()的时候继续尝试获取连接.如果设为true,
             那么尝试获取连接失败后该数据源将申明已经断开并永久关闭.默认为false**/
            dataSource.setBreakAfterAcquireFailure(true);
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return dataSource;
    }

    /**
     * 获取数据库连接
     * 根据数据源的不同
     * @param databaseParam
     * @return
     */
    public static final synchronized Connection getConnection(DatabaseParam databaseParam) {
        String dataSourceKey = databaseParam.getLinkedUrl() + ":" + databaseParam.getDriverClassName();
        ComboPooledDataSource dataSource = dataSourceMap.get(dataSourceKey);
        try {
            if (dataSource != null) {
                return dataSource.getConnection();
            }
            dataSource = getDataSource(databaseParam);
            dataSourceMap.put(dataSourceKey, dataSource);
            return dataSource.getConnection();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
