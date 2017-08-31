package com.nk.service;

import com.nk.dao.JdbcConnectionHolder;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

class TransactionalInvocationHandler implements InvocationHandler {

    private final DataSource dataSource;
    private final JdbcConnectionHolder connectionHolder;
    private final DogService dogService;

    TransactionalInvocationHandler(DataSource dataSource, JdbcConnectionHolder connectionHolder, DogService dogService) {
        this.dataSource = dataSource;
        this.connectionHolder = connectionHolder;
        this.dogService = dogService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return executeInTransaction(() -> method.invoke(dogService, args));
    }

    private <T> T executeInTransaction(TransactionalFunction<T> function) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connectionHolder.set(connection);
            boolean savedAutoCommit = connection.getAutoCommit();
            try {
                connection.setAutoCommit(false);
                T result = function.execute();
                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(savedAutoCommit);
            }
        }
    }

    @FunctionalInterface
    private interface TransactionalFunction<T> {
        T execute() throws Exception;
    }
}
