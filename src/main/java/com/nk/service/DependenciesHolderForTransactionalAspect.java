package com.nk.service;

import com.nk.dao.JdbcConnectionHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class DependenciesHolderForTransactionalAspect {
    private static DataSource dataSource;
    private static JdbcConnectionHolder connectionHolder;

    @Autowired
    public static void setDataSource(DataSource dataSource) {
        DependenciesHolderForTransactionalAspect.dataSource = dataSource;
    }

    @Autowired
    public static void setConnectionHolder(JdbcConnectionHolder connectionHolder) {
        DependenciesHolderForTransactionalAspect.connectionHolder = connectionHolder;
    }


    public static DataSource getDataSource() {
        return dataSource;
    }

    public static JdbcConnectionHolder getConnectionHolder() {
        return connectionHolder;
    }
}
