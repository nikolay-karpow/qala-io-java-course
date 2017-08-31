package com.nk.service;

import com.nk.dao.JdbcConnectionHolder;
import com.nk.webapp.Dog;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class TransactionalDogService implements DogService {

    private final DogService origin;
    private final DataSource dataSource;
    private final JdbcConnectionHolder connectionHolder;

    public TransactionalDogService(DogService origin, DataSource dataSource, JdbcConnectionHolder connectionHolder) {
        this.origin = origin;
        this.dataSource = dataSource;
        this.connectionHolder = connectionHolder;
    }

    @Override
    public Dog create(Dog dog) throws SQLException {
        return executeInTransaction(() -> origin.create(dog));
    }

    @Override
    public Dog findById(int id) throws SQLException {
        return executeInTransaction(() -> origin.findById(id));
    }

    @Override
    public Collection<Dog> listAll() throws SQLException {
        return executeInTransaction(() -> origin.listAll());
    }

    @Override
    public Dog update(Dog dog) throws SQLException {
        return executeInTransaction(() -> origin.update(dog));
    }

    @Override
    public boolean delete(int id) throws SQLException {
        return executeInTransaction(() -> origin.delete(id));
    }

    @FunctionalInterface
    private interface TransactionalFunction<T> {
        T execute() throws SQLException;
    }


    private <T> T executeInTransaction(TransactionalFunction<T> function) throws SQLException {
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
}
