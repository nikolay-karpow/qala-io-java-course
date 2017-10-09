package com.nk.service;

import com.nk.logging.Logged;
import com.nk.webapp.Dog;

import java.sql.SQLException;
import java.util.Collection;

public interface DogService {
    @Logged
    Dog create(Dog dog) throws SQLException;

    @Logged
    Dog findById(int id) throws SQLException;

    @Logged
    Collection<Dog> listAll() throws SQLException;

    @Logged
    Dog update(Dog dog) throws SQLException;

    @Logged
    boolean delete(int id) throws SQLException;
}
