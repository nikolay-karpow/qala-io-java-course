package com.nk.webapp;

import com.nk.validation.Name;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Dog {

    private int id;

    @Name
    private String name;

    @Past(message = "must be in the past")
    private Date birthday;

    @Min(value = 1, message = "must be greater than or equal to 1")
    private int height;

    @Min(value = 1, message = "must be greater than or equal to 1")
    private int weight;

    private Set<Paw> paws = new HashSet<>();

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
        this.birthday = new Date(birthday.getTime());
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


    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Set<Paw> getPaws() {
        return paws;
    }

    public void setPaws(Set<Paw> paws) {
        this.paws = paws;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dog dog = (Dog) o;

        return getId() == dog.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
