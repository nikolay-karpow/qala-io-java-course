package com.nk.webapp;

public class Paw {

    private int id;
    private String name;
    private Dog dog;

    public Paw() {
    }

    public Paw(String name, Dog dog) {
        this.name = name;
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
