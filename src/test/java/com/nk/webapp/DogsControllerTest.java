package com.nk.webapp;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

public class DogsControllerTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost:8080/sjc";
    }

    @Test
    public void testCreateDog() throws Exception {
        Dog bobik = new Dog("Bobik", new Date(), 10, 20);

        Response response = given().log().all()
                .contentType("application/json;charset=UTF-8")
                .body(bobik)
                .post("/dog");

        assertEquals(response.getStatusCode(), 201);

        String location = response.getHeader("Location");

        Dog dog = given().get(location).as(Dog.class);
        assertEquals(dog, bobik);
        assertNotEquals(dog.getId(), -1);
    }

    @Test
    public void testListDogs() throws Exception {
        Dog bobik = new Dog("NewBobik", new Date(), 10, 20);
        Dog tuzik = new Dog("NewTuzik", new Date(), 100, 200);

        given().contentType("application/json;charset=UTF-8")
                .body(bobik)
                .post("/dog");
        given().contentType("application/json;charset=UTF-8")
                .body(tuzik)
                .post("/dog");

        List<Dog> dogs = Arrays.asList(given().get("/dog").as(Dog[].class));
        assertTrue(dogs.contains(bobik));
        assertTrue(dogs.contains(tuzik));
    }
}
