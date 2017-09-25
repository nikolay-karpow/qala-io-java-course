package com.nk.service;

import com.nk.dao.DogDao;
import com.nk.logging.Logged;
import com.nk.webapp.Dog;

import java.sql.SQLException;
import java.util.Collection;

public class DogService {

    private final DogDao dogDao;

    // for the sake of CGLib
    protected DogService() {
        this(null);
    }

    public DogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Logged
    public Dog create(Dog dog) throws SQLException {
        return dogDao.create(dog);
    }

    @Logged
    public Dog findById(int id) throws SQLException {
        return dogDao.findById(id);
    }

    @Logged
    public Collection<Dog> listAll() throws SQLException {
        return dogDao.listAll();
    }

    @Logged
    public Dog update(Dog dog) throws SQLException {
        return dogDao.update(dog);
    }

    @Logged
    public boolean delete(int id) throws SQLException {
        return dogDao.delete(id);
    }
}
