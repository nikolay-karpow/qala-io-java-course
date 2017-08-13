package com.nk.dao;

import com.nk.webapp.Dog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static com.nk.webapp.DogUtil.randomDog;
import static io.qala.datagen.RandomShortApi.positiveInteger;
import static org.testng.Assert.*;
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

    @Test
    public void listAllGivesAllCreatedDogs() throws Exception {
        Dog firstDog = dogDao.create(randomDog());
        Dog secondDog = dogDao.create(randomDog());

        Collection<Dog> allDogs = dogDao.listAll();

        assertTrue(allDogs.contains(firstDog));
        assertTrue(allDogs.contains(secondDog));
    }

    @Test
    public void updateReplacesDogWhichWasSavedBefor() throws Exception {
        Dog createdDog = dogDao.create(randomDog());
        Dog changedDog = randomDog();
        changedDog.setId(createdDog.getId());

        Dog updatedDog = dogDao.update(changedDog);
        Dog foundDog = dogDao.findById(createdDog.getId());

        assertReflectionEquals(updatedDog, changedDog);
        assertReflectionEquals(foundDog, changedDog);
    }

    @Test
    public void updateThrowsExceptionWhenThereIsNoDogWithSuchId() throws Exception {
        Dog changedDog = randomDog();

        try {
            dogDao.update(changedDog);
            fail("Must not be executed");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Dog with id [" + changedDog.getId() + "] is not found");
        }
    }

    @Test
    public void deleteDogReturnsTrue_whenDogIsFoundAndDeleted() throws Exception {
        Dog dog = randomDog();
        dogDao.create(dog);

        boolean deleteResult = dogDao.delete(dog.getId());

        assertTrue(deleteResult);
        assertFalse(dogDao.listAll().contains(dog));
    }

    @Test
    public void deleteDogReturnsFalse_whenDogIsNotFound() throws Exception {
        boolean deleteResult = dogDao.delete(positiveInteger());

        assertFalse(deleteResult);
    }
}
