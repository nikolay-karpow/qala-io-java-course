package com.nk.webapp;

import java.util.Date;

public class Dog {
    private int id;
    private String name;
    private Date birthday;
    private int height;
    private int weight;

    public Dog() {
    }

    public Dog(String name, Date birthday, int height, int weight) {
        this(-1, name, birthday, height, weight);
    }

    public Dog(int id, String name, Date birthday, int height, int weight) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dog dog = (Dog) o;

        if (height != dog.height) return false;
        if (weight != dog.weight) return false;
        if (!name.equals(dog.name)) return false;
        return birthday != null ? birthday.equals(dog.birthday) : dog.birthday == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + height;
        result = 31 * result + weight;
        return result;
    }
}
