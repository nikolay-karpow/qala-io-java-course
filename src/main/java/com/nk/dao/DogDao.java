package com.nk.dao;

import com.nk.webapp.Dog;

import java.util.Collection;

public interface DogDao {

    Dog create(Dog dog);

    Dog findById(int id);

    Collection<Dog> listAll();

    Dog update(Dog dog);

    boolean delete(int id);
}
