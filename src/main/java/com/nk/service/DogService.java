package com.nk.service;

import com.nk.dao.DogDao;
import com.nk.webapp.Dog;

import java.sql.SQLException;
import java.util.Collection;

public class DogService {

    private final DogDao dogDao;

    public DogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    public Dog create(Dog dog) throws SQLException {
        return dogDao.create(dog);
    }

    public Dog findById(int id) throws SQLException {
        return dogDao.findById(id);
    }

    public Collection<Dog> listAll() throws SQLException {
        return dogDao.listAll();
    }

    public Dog update(Dog dog) throws SQLException {
        return dogDao.update(dog);
    }

    public boolean delete(int id) throws SQLException {
        return dogDao.delete(id);
    }
}
