package com.nk.webapp;

import io.qala.datagen.RandomValue;

import java.util.Date;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomValue.upTo;
import static java.lang.System.currentTimeMillis;

public class DogUtil {
    public static Dog randomDog() {
        return new Dog(alphanumeric(1, 100),
                RandomValue.upTo(currentTimeMillis()).date(), positiveInteger(), positiveInteger());
    }


    public static Date randomValidBirthday() {
        return upTo(new Date()).date();
    }

    public static String randomValidName() {
        return alphanumeric(1, 100);
    }

    public static Dog dogWithName(String name) {
        return new Dog(name, new Date(currentTimeMillis() - 1), 1, 2);
    }

    public static Dog dogWithDate(Date birthday) {
        Dog dog = randomDog();
        dog.setBirthday(birthday);
        return dog;
    }

    public static Dog dogWithHeight(int height) {
        Dog dog = randomDog();
        dog.setHeight(height);
        return dog;
    }

    public static Dog dogWithWeight(int weight) {
        Dog dog = randomDog();
        dog.setWeight(weight);
        return dog;
    }
}
