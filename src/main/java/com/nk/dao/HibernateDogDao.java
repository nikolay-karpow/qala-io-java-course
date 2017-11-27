package com.nk.dao;

import com.nk.webapp.Dog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;

public class HibernateDogDao implements DogDao {

    private final SessionFactory sessionFactory;

    public HibernateDogDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Dog create(Dog dog) {
        session().save(dog);
        return dog;
    }

    @Override
    public Dog findById(int id) {
        return session().get(Dog.class, id);
    }

    @Override
    public Collection<Dog> listAll() {
        return session().createQuery("from Dog").list();
    }

    @Override
    public Dog update(Dog dog) {
        if (session().get(Dog.class, dog.getId()) != null) {
            session().merge(dog);
            return dog;
        }
        throw new IllegalArgumentException("Dog with id [" + dog.getId() + "] is not found");
    }

    @Override
    public boolean delete(int id) {
        Dog dog = session().get(Dog.class, id);
        if (dog != null) {
            session().delete(dog);
            return true;
        }
        return false;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
