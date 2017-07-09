package com.nk.webapp;

import java.util.Collection;
import java.util.Date;

public class Dog {
    private final String name;
    private final Date birthday;
    private final int height;

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

    private final int weight;

    public Dog(String name, Date birthday, int height, int weight) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
    }
}
