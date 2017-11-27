package com.nk.service;

import com.nk.dao.DogDao;
import com.nk.logging.Logged;
import com.nk.webapp.Dog;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Dog create(Dog dog) {
        return dogDao.create(dog);
    }

    @Override
    @Logged
    @Transactional(readOnly = true)
    public Dog findById(int id) {
        return dogDao.findById(id);
    }

    @Override
    @Logged
    @Transactional(readOnly = true)
    public Collection<Dog> listAll() {
        return dogDao.listAll();
    }

    @Override
    @Logged
    @Transactional
    public Dog update(Dog dog) {
        return dogDao.update(dog);
    }

    @Override
    @Logged
    @Transactional
    public boolean delete(int id) {
        return dogDao.delete(id);
    }
}
