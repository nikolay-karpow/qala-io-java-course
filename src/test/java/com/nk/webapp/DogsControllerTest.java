package com.nk.webapp;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;

public class DogsControllerTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost:8080/sjc";
    }

    @Test
    public void testCreateDog() throws Exception {
        Response response = RestAssured.given()
                .contentType("application/json;charset=UTF-8")
                .body(new Dog("Bobik", new Date(), 10, 20))
                .post("/dog");

        assertEquals(response.getStatusCode(), 201);
    }

    @Test
    public void testName() throws Exception {
        Response response = RestAssured.given()
                .get("/test")
                .andReturn();
        System.err.println(response.getStatusCode());
    }
}
