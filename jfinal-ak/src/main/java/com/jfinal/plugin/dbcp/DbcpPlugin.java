package com.jfinal.plugin.dbcp;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;

public class DbcpPlugin implements IPlugin, IDataSourceProvider {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String driverClass = "com.mysql.jdbc.Driver";
    private String url;
    private String username;
    private String password;
    private int initSize = 10;
    private int maxActive = 100;
    private int maxIdle = 30;
    private boolean defaultAutoCommit = true;
    private boolean logAbandoned = true;
    private int removeAbandonedTimeout = 180;
    private BasicDataSource dataSource;

    public DbcpPlugin(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean start() {
        this.dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);

        dataSource.setInitialSize(initSize);
        dataSource.setMaxTotal(maxActive);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMinIdle(5);
        dataSource.setMaxWaitMillis(600000);
        dataSource.setDefaultAutoCommit(defaultAutoCommit);
        dataSource.setLogAbandoned(logAbandoned);
        dataSource.setMinEvictableIdleTimeMillis(10000);
        dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        dataSource.setValidationQuery("select 1;");
        return true;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public boolean stop() {
        if(this.dataSource != null) {
            try {
                this.dataSource.close();
            } catch (SQLException e) {
                logger.error("error in close datasource:", 3);
            }
        }
        return true;
    }
}
