package com.nk.dao;

import com.nk.webapp.Dog;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Collection;

import static com.nk.webapp.DogUtil.randomDog;
import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.positiveInteger;
import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class HibernateDogDaoTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DogDao dogDao;

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
    public void findByIdReturnsNullWhenDogIsNotFound() throws Exception {
        Dog foundDog = dogDao.findById(positiveInteger());
        assertTrue(foundDog == null);
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
        createdDog = updateFieldsToRandomValues(createdDog);


        Dog updatedDog = dogDao.update(createdDog);
        Dog foundDog = dogDao.findById(createdDog.getId());

        assertReflectionEquals(updatedDog, createdDog);
        assertReflectionEquals(foundDog, createdDog);
    }

    @Test
    public void updateThrowsExceptionWhenThereIsNoDogWithSuchId() throws Exception {
        Dog dog = randomDog();

        try {
            dogDao.update(dog);
            fail("Must not be executed");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Dog with id [" + dog.getId() + "] is not found");
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

    @Test
    public void dogWithNameOfMaximumLengthCanBeSaved() throws Exception {
        Dog dog = randomDog();
        dog.setName(alphanumeric(100));

        Dog createdDog = dogDao.create(dog);

        dog.setId(createdDog.getId());
        assertReflectionEquals(dog, createdDog);
    }

    @Test
    public void dogCreationIsNotVolnurableForSqlInjectionInDogName() throws Exception {
        Dog dog = randomDog();
        dog.setName("\"' blah");

        Dog createdDog = dogDao.create(dog);

        assertEquals(createdDog.getName(), dog.getName());
    }


    @Test
    public void updateIsNotVolnurableForSqlInjectionInDogName() throws Exception {
        Dog createdDog = dogDao.create(randomDog());
        createdDog = updateFieldsToRandomValues(createdDog);

        String name = "\"' blah";
        createdDog.setName(name);
        Dog updatedDog = dogDao.update(createdDog);

        assertReflectionEquals(updatedDog, createdDog);
    }

    private Dog updateFieldsToRandomValues(Dog dog) {
        Dog randomDog = randomDog();
        dog.setName(randomDog.getName());
        dog.setBirthday(randomDog.getBirthday());
        dog.setHeight(randomDog.getHeight());
        dog.setWeight(randomDog.getWeight());
        return dog;
    }
}
