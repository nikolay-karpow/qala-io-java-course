package com.nk.webapp;

import groovy.util.logging.Slf4j;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

import static java.lang.System.currentTimeMillis;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Slf4j
public class DogValidationTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    public static final String TOO_LONG_NAME = "Aqwerj a;lkj wlkerj iu paoisud fqlwenr .,n.,n  kjqwlkrej nm.,samndf qkjwer; l kja ;sdf qwirj;lkjpoiue";

    @Test
    public void name_mustBeNonEmptyUpTo100Symbols() throws Exception {
        String expectedMessage = "size must be between 1 and 100";
        checkDogWithOneInvalidField(dogWithName(""), expectedMessage);
        checkDogWithOneInvalidField(dogWithName(TOO_LONG_NAME), expectedMessage);
        checkValid(dogWithName("B"));
        checkValid(dogWithName(null));
        checkValid(dogWithName(TOO_LONG_NAME.substring(0, 100)));
    }

    @Test
    public void dateOfBirth_mustNotBeInTheFuture() throws Exception {
        checkValid(dogWithDate(null));
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

    private Dog dogWithName(String name) {
        return new Dog(name, new Date(currentTimeMillis() - 1), 1, 2);
    }

    private Dog dogWithDate(Date birthday) {
        return new Dog("Bobik", birthday, 1, 2);
    }

    private Dog dogWithHeight(int height) {
        return new Dog("Bobik", new Date(currentTimeMillis() - 1), height, 3);
    }

    private Dog dogWithWeight(int weight) {
        return new Dog("Bobik", new Date(currentTimeMillis() - 1), 4, weight);
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
