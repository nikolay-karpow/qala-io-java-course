package com.nk.dao;

import com.nk.webapp.Dog;
import org.h2.jdbcx.JdbcDataSource;

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
    private static final String DELETE = "DELETE FROM Dog where id=?";

    private final JdbcDataSource dataSource;
    private final Connection connection;

    public JdbcDogDao() throws SQLException {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:ddl.sql'");
        connection = dataSource.getConnection();
    }

    @Override
    public Dog create(Dog dog) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(CREATE_DOG, RETURN_GENERATED_KEYS)) {
            setSaveStatementParameters(dog, statement);
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    dog.setId(resultSet.getInt(1));
                    return dog;
                } else {
                    throw new IllegalStateException(); //TODO: something more specific
                }
            }
        }
    }

    @Override
    public Dog findById(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return dog(resultSet);
                } else {
                    throw new IllegalStateException(); //TODO: something more specific
                }
            }
        }
    }

    @Override
    public Collection<Dog> listAll() throws SQLException {
        try (Statement statement = connection.createStatement();
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
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setSaveStatementParameters(dog, statement);
            statement.setInt(5, dog.getId());
            int updatedCount = statement.executeUpdate();
            if (updatedCount == 1) {
                return dog;
            } else {
                throw new IllegalArgumentException("Dog with id [" + dog.getId() + "] is not found");
            }
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
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
}
