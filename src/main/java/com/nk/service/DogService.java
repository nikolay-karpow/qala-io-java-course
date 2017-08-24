package com.nk.service;

import com.nk.dao.DogDao;
import com.nk.dao.JdbcConnectionHolder;
import com.nk.webapp.Dog;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class DogService {

    private final DogDao dogDao;
    private final DataSource dataSource;
    private final JdbcConnectionHolder connectionHolder;

    public DogService(DogDao dogDao, DataSource dataSource, JdbcConnectionHolder connectionHolder) {
        this.dogDao = dogDao;
        this.dataSource = dataSource;
        this.connectionHolder = connectionHolder;
    }

    public Dog create(Dog dog) throws SQLException {
        return executeInTransaction(() -> dogDao.create(dog));
    }

    public Dog findById(int id) throws SQLException {
        return executeInTransaction(() -> dogDao.findById(id));
    }

    public Collection<Dog> listAll() throws SQLException {
        return executeInTransaction(dogDao::listAll);
    }

    public Dog update(Dog dog) throws SQLException {
        return executeInTransaction(() -> dogDao.update(dog));
    }

    public boolean delete(int id) throws SQLException {
        return executeInTransaction(() -> dogDao.delete(id));
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
