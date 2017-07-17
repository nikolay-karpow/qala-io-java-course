package com.nk.webapp;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.LogDetail;
import com.jayway.restassured.http.ContentType;
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
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void testCreateDog() throws Exception {
        Dog bobik = new Dog("Bobik", new Date(), 10, 20);

        Response response = createDog(bobik);

        assertEquals(response.getStatusCode(), 201);

        String location = response.getHeader("Location");

        Dog dog = getDog(location);
        assertEquals(dog, bobik);
        assertNotEquals(dog.getId(), -1);
    }

    @Test
    public void testGetDogWithWrongId() throws Exception {
        getDogResponse("/dog/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);

        getDogResponse("/dog/-1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testListDogs() throws Exception {
        Dog bobik = new Dog("NewBobik", new Date(), 10, 20);
        Dog tuzik = new Dog("NewTuzik", new Date(), 100, 200);

       createDog(bobik);
       createDog(tuzik);

        List<Dog> dogs = Arrays.asList(getDogResponse("/dog").as(Dog[].class));
        assertTrue(dogs.contains(bobik));
        assertTrue(dogs.contains(tuzik));
    }

    @Test
    public void testDeleteDog() throws Exception {
        Dog bobik = new Dog("Bobik", new Date(), 10, 20);
        Response response = createDog(bobik);

        String location = response.getHeader("Location");
        Response deleteResponse = given().delete(location);

        assertEquals(deleteResponse.statusCode(), 200);
        assertEquals(deleteResponse.getBody().asString(), "Dog is deleted");
        assertEquals(getDogResponse(location).statusCode(), 404);
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
        Response response = createDog(new Dog("Bobik", new Date(), 10, 20));

        String location = response.getHeader("Location");

        Dog dog = getDog(location);
        dog.setName("Tuzik");
        dog.setHeight(500);

        given().body(dog)
                .put("/dog")
                .then()
                .statusCode(200);

        Dog retrievedAfterPut = getDog(location);
        assertEquals(retrievedAfterPut, dog);
    }

    @Test
    public void serviceReturnsBadRequest_whenInvalidDogIsCreated() throws Exception {
        Dog dog = new Dog("", new Date(), 10, 20);
        Response response = createDog(dog);

        assertEquals(response.getStatusCode(), 400);
    }

    @Test
    public void serviceReturnsBadRequest_whenDogReplacementIsInvalid() throws Exception {
        Response response = createDog(new Dog("Bobik", new Date(), 10, 20));

        String location = response.getHeader("Location");

        Dog dog = getDog(location);
        dog.setName("Tuzik");
        dog.setHeight(-1);

        given().body(dog)
                .put("/dog")
                .then()
                .statusCode(400);
    }

    private Response createDog(Dog dog) {
        return given().log().all()
                    .body(dog)
                    .post("/dog");
    }

    private Dog getDog(String uri) {
        return getDogResponse(uri).as(Dog.class);
    }

    private Response getDogResponse(String uri) {
        return given().get(uri);
    }
}
