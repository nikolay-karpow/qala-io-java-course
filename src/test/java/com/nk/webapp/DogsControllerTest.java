package com.nk.webapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static com.nk.webapp.DogUtil.randomDog;
import static io.qala.datagen.RandomShortApi.positiveInteger;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@WebAppConfiguration
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class DogsControllerTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeMethod
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
    }

    @Test
    public void testCreateDog() throws Exception {
        Dog newDog = randomDog();

        MockHttpServletResponse response = createDog(newDog);

        Assert.assertEquals(response.getStatus(), 201);

        String location = response.getHeader("Location");

        Dog dog = getDog(location);
        newDog.setId(dog.getId());
        assertReflectionEquals(dog, newDog);
    }

    @Test
    public void testGetDogWithWrongId() throws Exception {
        MockHttpServletResponse response = getDogResponse("/dog/" + Integer.MAX_VALUE);
        assertEquals(response.getStatus(), 404);

        response = getDogResponse("/dog/-1");
        assertEquals(response.getStatus(), 404);
    }


    @Test
    public void testListDogs() throws Exception {
        Dog firstDog = randomDog();
        Dog secondDog = randomDog();

        MockHttpServletResponse firstResponse = createDog(firstDog);
        firstDog.setId(idFromLocation(firstResponse.getHeader("Location")));
        MockHttpServletResponse secondResponse = createDog(secondDog);
        secondDog.setId(idFromLocation(secondResponse.getHeader("Location")));

        List<Dog> dogs = OBJECT_MAPPER.readerFor(new TypeReference<List<Dog>>() {
        })
            .readValue(getDogResponse("/dog").getContentAsByteArray());
        assertTrue(dogs.contains(firstDog));
        assertTrue(dogs.contains(secondDog));
    }

    private int idFromLocation(String location) {
        String[] parts = location.split("/");
        return Integer.valueOf(parts[parts.length - 1]);
    }

    @Test
    public void testDeleteDog() throws Exception {
        Dog dog = randomDog();
        MockHttpServletResponse response = createDog(dog);

        String location = response.getHeader("Location");
        MockHttpServletResponse deleteResponse = mvc.perform(delete(location)).andReturn().getResponse();

        Assert.assertEquals(deleteResponse.getStatus(), 200);
        Assert.assertEquals(deleteResponse.getContentAsString(), "Dog is deleted");
        Assert.assertEquals(getDogResponse(location).getStatus(), 404);
    }

    @Test
    public void testDeleteDogWrongId() throws Exception {
        mvc.perform(delete("/dog/" + Integer.MAX_VALUE))
            .andExpect(status().is(404));

        mvc.perform(delete("/dog/-1"))
            .andExpect(status().is(404));
    }

    @Test
    public void testReplaceDog() throws Exception {
        Dog bobik = randomDog();
        MockHttpServletResponse response = createDog(bobik);

        String location = response.getHeader("Location");

        Dog dog = getDog(location);
        dog.setName(DogUtil.randomValidName());
        dog.setBirthday(DogUtil.randomValidBirthday());
        dog.setHeight(positiveInteger());
        dog.setWeight(positiveInteger());

        mvc.perform(put("/dog")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(OBJECT_MAPPER.writeValueAsString(dog)))
            .andExpect(status().is(200));

        Dog retrievedAfterPut = getDog(location);
        Assert.assertEquals(retrievedAfterPut, dog);
    }

    @Test
    public void serviceReturnsBadRequest_whenInvalidDogIsCreated() throws Exception {
        Dog dog = new Dog("", new Date(), 10, 20);
        MockHttpServletResponse response = createDog(dog);

        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void serviceReturnsBadRequest_whenDogReplacementIsInvalid() throws Exception {
        MockHttpServletResponse response = createDog(randomDog());

        String location = response.getHeader("Location");

        Dog dog = getDog(location);
        dog.setName(DogUtil.randomValidName());
        dog.setHeight(-1);

        response = mvc.perform(put("/dog")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(OBJECT_MAPPER.writeValueAsBytes(dog)))
            .andReturn().getResponse();

        assertEquals(response.getStatus(), 400);
    }

    private MockHttpServletResponse createDog(Dog dog) throws Exception {
        return mvc.perform(post("/dog")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(OBJECT_MAPPER.writeValueAsBytes(dog)))
            .andReturn().getResponse();
    }

    private Dog getDog(String uri) throws Exception {
        byte[] body = getDogResponse(uri)
            .getContentAsByteArray();
        return OBJECT_MAPPER.readerFor(Dog.class).readValue(body);
    }

    private MockHttpServletResponse getDogResponse(String uri) throws Exception {
        return mvc.perform(get(uri))
            .andReturn()
            .getResponse();
    }

}
