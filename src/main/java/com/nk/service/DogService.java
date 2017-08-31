package com.nk.service;

import com.nk.webapp.Dog;

import java.sql.SQLException;
import java.util.Collection;

public interface DogService {

    Dog create(Dog dog) throws SQLException;

    Dog findById(int id) throws SQLException;

    Collection<Dog> listAll() throws SQLException;

    Dog update(Dog dog) throws SQLException;

    boolean delete(int id) throws SQLException;
}
