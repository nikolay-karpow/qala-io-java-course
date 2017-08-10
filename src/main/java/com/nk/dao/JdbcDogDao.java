package com.nk.dao;

import com.nk.webapp.Dog;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.time.Instant;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.time.Instant.ofEpochMilli;
import static java.util.Date.from;

public class JdbcDogDao implements DogDao {

    private static final String CREATE_DOG = "INSERT INTO Dog (name, birthday, height, weight) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM Dog WHERE id=?";
    private final JdbcDataSource dataSource;
    Connection connection;

    public JdbcDogDao() throws SQLException {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:ddl.sql'");
        connection = dataSource.getConnection();
    }

    @Override
    public Dog create(Dog dog) throws SQLException {

        try (
             PreparedStatement statement = connection.prepareStatement(CREATE_DOG, RETURN_GENERATED_KEYS)) {
            statement.setString(1, dog.getName());
            statement.setLong(2, dog.getBirthday().toInstant().toEpochMilli());
            statement.setInt(3, dog.getHeight());
            statement.setInt(4, dog.getWeight());
            int i = statement.executeUpdate();
            System.out.println(i);

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
                System.out.println(statement.toString());
                if (resultSet.next()) {
                    return new Dog(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            from(ofEpochMilli(resultSet.getLong("birthday"))),
                            resultSet.getInt("height"),
                            resultSet.getInt("weight")
                    );
                } else {
                    throw new IllegalStateException(); //TODO: something more specific
                }
            }
        }
    }

    @Override
    public Collection<Dog> listAll() {
        return null;
    }

    @Override
    public Dog update(Dog dog) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
