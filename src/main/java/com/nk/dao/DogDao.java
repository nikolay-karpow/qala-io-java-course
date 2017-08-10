package com.nk.dao;

import com.nk.webapp.Dog;

import java.sql.SQLException;
import java.util.Collection;

public interface DogDao {

    Dog create(Dog dog) throws SQLException;

    Dog findById(int id) throws SQLException;

    Collection<Dog> listAll();

    Dog update(Dog dog);

    boolean delete(int id);
}
