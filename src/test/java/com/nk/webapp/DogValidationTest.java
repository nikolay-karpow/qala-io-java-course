package com.nk.webapp;

import groovy.util.logging.Slf4j;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

import static com.nk.webapp.DogUtil.*;
import static io.qala.datagen.RandomShortApi.alphanumeric;
import static java.lang.System.currentTimeMillis;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Slf4j
public class DogValidationTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void name_mustBeNonEmptyUpTo100Symbols() throws Exception {
        String expectedMessage = "Name must be non-null, non-empty and not longer, than 100 characters";
        checkDogWithOneInvalidField(dogWithName(""), expectedMessage);
        checkDogWithOneInvalidField(dogWithName(null), expectedMessage);
        checkDogWithOneInvalidField(dogWithName(alphanumeric(101, 1000)), expectedMessage);
        checkValid(dogWithName(alphanumeric(1, 100)));
    }

    @Test
    public void dateOfBirth_mustNotBeInTheFuture() throws Exception {
        checkValid(dogWithDate(new Date(currentTimeMillis() - 1)));
        checkDogWithOneInvalidField(dogWithDate(new Date(currentTimeMillis() + 5000)), "must be in the past");
    }

    @Test
    public void height_mustBeGreaterThanZero() throws Exception {
        checkValid(dogWithHeight(1));
        checkValid(dogWithHeight(20));
        checkDogWithOneInvalidField(dogWithHeight(0), "must be greater than or equal to 1");
        checkDogWithOneInvalidField(dogWithHeight(-1), "must be greater than or equal to 1");
    }

    @Test
    public void weight_mustBeGreaterThanZero() throws Exception {
        checkValid(dogWithWeight(1));
        checkValid(dogWithWeight(20));
        checkDogWithOneInvalidField(dogWithWeight(0), "must be greater than or equal to 1");
        checkDogWithOneInvalidField(dogWithWeight(-1), "must be greater than or equal to 1");
    }

    private void checkDogWithOneInvalidField(Dog dog, String expectedMessage) {
        Set<ConstraintViolation<Dog>> constraintViolations = validator.validate(dog);
        assertEquals(1, constraintViolations.size());
        assertEquals(constraintViolations.iterator().next().getMessage(), expectedMessage);
    }

    private void checkValid(Dog dog) {
        Set<ConstraintViolation<Dog>> validate = validator.validate(dog);
        if (!validate.isEmpty()) {
            for (ConstraintViolation<Dog> constraintViolation : validate) {
                System.err.println(constraintViolation.getMessage());
            }
        }
        assertTrue(validate.isEmpty());
    }
}
