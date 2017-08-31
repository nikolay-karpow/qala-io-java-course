package com.nk.service;

import com.nk.dao.JdbcConnectionHolder;
import com.nk.webapp.Dog;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class TransactionalProxy implements DogService {

    private final DogService proxiedOrigin;

    public TransactionalProxy(DogService origin, DataSource dataSource, JdbcConnectionHolder connectionHolder) {
        this.proxiedOrigin = (DogService) Proxy.newProxyInstance(
            origin.getClass().getClassLoader(),
            origin.getClass().getInterfaces(),
            new TransactionalInvocationHandler(dataSource, connectionHolder, origin)
        );
    }

    @Override
    public Dog create(Dog dog) throws SQLException {
        return proxiedOrigin.create(dog);
    }

    @Override
    public Dog findById(int id) throws SQLException {
        return proxiedOrigin.findById(id);
    }

    @Override
    public Collection<Dog> listAll() throws SQLException {
        return proxiedOrigin.listAll();
    }

    @Override
    public Dog update(Dog dog) throws SQLException {
        return proxiedOrigin.update(dog);
    }

    @Override
    public boolean delete(int id) throws SQLException {
        return proxiedOrigin.delete(id);
    }

    private static class TransactionalInvocationHandler implements InvocationHandler {

        private final DataSource dataSource;
        private final JdbcConnectionHolder connectionHolder;
        private final DogService dogService;

        private TransactionalInvocationHandler(DataSource dataSource, JdbcConnectionHolder connectionHolder, DogService dogService) {
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
}
