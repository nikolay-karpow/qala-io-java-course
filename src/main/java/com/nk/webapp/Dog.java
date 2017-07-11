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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
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

    public void setWeight(int weight) {
        this.weight = weight;
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
