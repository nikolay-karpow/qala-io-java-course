package com.nk.webapp;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.*;

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
    public void testGetDogWithWrongId() throws Exception {
        given().get("/dog/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);

        given().get("/dog/-1")
                .then()
                .statusCode(404);
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

    @Test
    public void testDeleteDog() throws Exception {
        Dog bobik = new Dog("Bobik", new Date(), 10, 20);
        Response response = given().log().all()
                .contentType("application/json;charset=UTF-8")
                .body(bobik)
                .post("/dog");

        String location = response.getHeader("Location");
        Response deleteResponse = given().delete(location);

        assertEquals(deleteResponse.statusCode(), 200);
        assertEquals(deleteResponse.getBody().asString(), "Dog is deleted");
        assertEquals(given().get(location).statusCode(), 404);
    }

    @Test
    public void testDeleteDogWrongId() throws Exception {
        given().delete("/dog/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);

        given().delete("/dog/-1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReplaceDog() throws Exception {
        Response response = given().log().all()
                .contentType("application/json;charset=UTF-8")
                .body(new Dog("Bobik", new Date(), 10, 20))
                .post("/dog");

        String location = response.getHeader("Location");

        Dog dog = given().get(location).as(Dog.class);
        dog.setName("Tuzik");
        dog.setHeight(500);

        given().body(dog)
                .contentType("application/json;charset=UTF-8")
                .put("/dog")
                .then()
                .statusCode(200);

        Dog retrievedAfterPut = given().get(location).as(Dog.class);
        assertEquals(retrievedAfterPut, dog);
    }
}
