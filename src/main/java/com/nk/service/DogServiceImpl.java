package com.nk.service;

import com.nk.dao.DogDao;
import com.nk.webapp.Dog;

import java.sql.SQLException;
import java.util.Collection;

public class DogServiceImpl implements DogService {

    private final DogDao dogDao;

    public DogServiceImpl(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Override
    public Dog create(Dog dog) throws SQLException {
        return dogDao.create(dog);
    }

    @Override
    public Dog findById(int id) throws SQLException {
        return dogDao.findById(id);
    }

    @Override
    public Collection<Dog> listAll() throws SQLException {
        return dogDao.listAll();
    }

    @Override
    public Dog update(Dog dog) throws SQLException {
        return dogDao.update(dog);
    }

    @Override
    public boolean delete(int id) throws SQLException {
        return dogDao.delete(id);
    }

}
