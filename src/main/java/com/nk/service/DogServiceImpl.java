package com.nk.service;

import com.nk.dao.DogDao;
import com.nk.logging.Logged;
import com.nk.webapp.Dog;

import java.sql.SQLException;
import java.util.Collection;

public class DogServiceImpl implements DogService {

    private final DogDao dogDao;

    // for the sake of CGLib
    protected DogServiceImpl() {
        this(null);
    }

    public DogServiceImpl(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Override
    @Logged
    public Dog create(Dog dog) throws SQLException {
        return dogDao.create(dog);
    }

    @Override
    @Logged
    public Dog findById(int id) throws SQLException {
        return dogDao.findById(id);
    }

    @Override
    @Logged
    public Collection<Dog> listAll() throws SQLException {
        return dogDao.listAll();
    }

    @Override
    @Logged
    public Dog update(Dog dog) throws SQLException {
        return dogDao.update(dog);
    }

    @Override
    @Logged
    public boolean delete(int id) throws SQLException {
        return dogDao.delete(id);
    }
}
