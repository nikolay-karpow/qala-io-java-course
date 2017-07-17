package com.nk.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.testng.AssertJUnit.assertEquals;

@WebAppConfiguration
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class DogsControllerTest extends AbstractTestNGSpringContextTests {

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
    public void serviceReturnsBadRequest_whenInvalidDogIsCreated() throws Exception {
        Dog dog = new Dog("", new Date(), 10, 20);
        MockHttpServletResponse response = createDog(dog);

        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void serviceReturnsBadRequest_whenDogReplacementIsInvalid() throws Exception {
        MockHttpServletResponse response = createDog(new Dog("Bobik", new Date(), 10, 20));

        String location = response.getHeader("Location");

        Dog dog = getDog(location);
        dog.setName("Tuzik");
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
        byte[] body = mvc.perform(get(uri))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return OBJECT_MAPPER.readerFor(Dog.class).readValue(body);
    }

}
