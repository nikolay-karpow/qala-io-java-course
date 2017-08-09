package com.nk.dao;

import com.nk.webapp.Dog;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDogDao implements DogDao {

    private final static AtomicInteger idCounter = new AtomicInteger();
    private final static Map<Integer, Dog> dogs = new ConcurrentHashMap<>();

    @Override
    public Dog create(Dog dog) {
        dog.setId(idCounter.incrementAndGet());
        dogs.put(dog.getId(), dog);
        return dog;
    }

    @Override
    public Dog findById(int id) {
        return dogs.get(id);
    }

    @Override
    public Collection<Dog> listAll() {
        return dogs.values();
    }

    @Override
    public Dog update(Dog dog) {
        dogs.put(dog.getId(), dog);
        return dog;
    }

    @Override
    public boolean delete(int id) {
        return dogs.remove(id) != null;
    }


}
