package com.nk.dao;

import com.nk.webapp.Dog;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcDogDao implements DogDao {

    private static final String CREATE_DOG = "INSERT INTO Dog (name, birthday, height, weight) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM Dog WHERE id=?";
    private static final String LIST_ALL = "SELECT * FROM Dog";
    private static final String UPDATE = "UPDATE Dog SET name=?, birthday=?, height=?, weight=? WHERE id=?";
    private static final String DELETE = "DELETE FROM Dog WHERE id=?";

    private final DataSource dataSource;

    public JdbcDogDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Dog create(Dog dog) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_DOG, RETURN_GENERATED_KEYS)) {
            setSaveStatementParameters(dog, statement);

            return executeInTransaction(connection, () -> {
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        dog.setId(resultSet.getInt(1));
                        return dog;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            });
        }
    }

    @Override
    public Dog findById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return dog(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public Collection<Dog> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(LIST_ALL)) {
            List<Dog> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(dog(resultSet));
            }
            return result;
        }
    }

    @Override
    public Dog update(Dog dog) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setSaveStatementParameters(dog, statement);
            statement.setInt(5, dog.getId());

            return executeInTransaction(connection, () -> {
                int updatedCount = statement.executeUpdate();
                if (updatedCount == 1) {
                    return dog;
                } else {
                    throw new IllegalArgumentException("Dog with id [" + dog.getId() + "] is not found");
                }
            });
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            return executeInTransaction(connection, () -> statement.executeUpdate() == 1);
        }
    }

    private Dog dog(ResultSet resultSet) throws SQLException {
        return new Dog(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            Date.from(resultSet.getTimestamp("birthday").toInstant()),
            resultSet.getInt("height"),
            resultSet.getInt("weight")
        );
    }

    private void setSaveStatementParameters(Dog dog, PreparedStatement statement) throws SQLException {
        statement.setString(1, dog.getName());
        statement.setTimestamp(2, Timestamp.from(dog.getBirthday().toInstant()));
        statement.setInt(3, dog.getHeight());
        statement.setInt(4, dog.getWeight());
    }

    @FunctionalInterface
    private interface TransactionalFunction<T> {
        T execute() throws SQLException;
    }

    private <T> T executeInTransaction(Connection connection, TransactionalFunction<T> function) throws SQLException {
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
