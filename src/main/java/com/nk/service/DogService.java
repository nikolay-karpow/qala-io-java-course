package com.nk.service;

import com.nk.logging.Logged;
import com.nk.webapp.Dog;

import java.util.Collection;

public interface DogService {
    @Logged
    Dog create(Dog dog);

    @Logged
    Dog findById(int id);

    @Logged
    Collection<Dog> listAll();

    @Logged
    Dog update(Dog dog);

    @Logged
    boolean delete(int id);
}
