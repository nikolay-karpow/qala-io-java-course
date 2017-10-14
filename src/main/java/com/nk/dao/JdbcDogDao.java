package com.nk.dao;

import com.nk.webapp.Dog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.stream.Collectors.toList;

public class JdbcDogDao implements DogDao {

    private static final String CREATE_DOG = "INSERT INTO Dog (name, birthday, height, weight) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM Dog WHERE id=?";
    private static final String LIST_ALL = "SELECT * FROM Dog";
    private static final String UPDATE = "UPDATE Dog SET name=?, birthday=?, height=?, weight=? WHERE id=?";
    private static final String DELETE = "DELETE FROM Dog WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcDogDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Dog create(Dog dog) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_DOG, RETURN_GENERATED_KEYS);
            setSaveStatementParameters(dog, statement);
            return statement;
        }, generatedKeyHolder);
        dog.setId(generatedKeyHolder.getKey().intValue());
        return dog;
    }

    @Override
    public Dog findById(int id) {
        return jdbcTemplate.queryForList(FIND_BY_ID, id).stream()
                .map(this::dog)
                .findFirst().orElse(null);
    }

    @Override
    public Collection<Dog> listAll() {
        return jdbcTemplate.queryForList(LIST_ALL).stream()
                .map(this::dog)
                .collect(toList());
    }

    @Override
    public Dog update(Dog dog) {
        int updatedCount = jdbcTemplate.update(UPDATE,
                dog.getName(),
                Timestamp.from(dog.getBirthday().toInstant()),
                dog.getHeight(),
                dog.getWeight(),
                dog.getId()
        );

        if (updatedCount == 1) {
            return dog;
        } else {
            throw new IllegalArgumentException("Dog with id [" + dog.getId() + "] is not found");
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE, id) == 1;
    }

    private Dog dog(Map<String, Object> row) {
        return new Dog(
                parseInt(valueOf(row.get("id"))),
                String.valueOf(row.get("name")),
                parseBirthday(row),
                parseInt(valueOf(row.get("height"))),
                parseInt(valueOf(row.get("weight")))
        );
    }

    private java.util.Date parseBirthday(Map<String, Object> row) {
        return java.util.Date.from(((Timestamp) row.get("birthday")).toInstant());
    }

    private void setSaveStatementParameters(Dog dog, PreparedStatement statement) throws SQLException {
        statement.setString(1, dog.getName());
        statement.setTimestamp(2, Timestamp.from(dog.getBirthday().toInstant()));
        statement.setInt(3, dog.getHeight());
        statement.setInt(4, dog.getWeight());
    }
}
 