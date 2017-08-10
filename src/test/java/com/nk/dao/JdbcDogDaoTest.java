package com.nk.dao;

import com.nk.webapp.Dog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static com.nk.webapp.DogUtil.randomDog;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JdbcDogDaoTest {
    private static DogDao dogDao;

    @BeforeClass
    public static void beforeClass() throws SQLException {
        dogDao = new JdbcDogDao();
    }

    @Test
    public void createDog_returnsDogWithNewId() throws Exception {
        Dog dog = randomDog();
        Dog createdDog = dogDao.create(dog);
        assertTrue(createdDog.getId() >= 0);
        dog.setId(createdDog.getId());

        assertReflectionEquals(createdDog, dog);
    }

    @Test
    public void createdDogCanBeFoundById() throws Exception {
        Dog createdDog = dogDao.create(randomDog());

        Dog foundDog = dogDao.findById(createdDog.getId());

        assertReflectionEquals(createdDog, foundDog);
    }
}
