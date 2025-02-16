package com.example.weatherapianalysis.utils.validator;

import com.example.weatherapianalysis.base.AbstractBaseServiceTest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityValidatorTest extends AbstractBaseServiceTest {

    @InjectMocks
    private CityValidator cityValidator;

    @Mock
    private ConstraintValidatorContext context;

    @Test
    void testValidCities() {
        assertTrue(cityValidator.isValid("London", context));
        assertTrue(cityValidator.isValid("Barcelona", context));
        assertTrue(cityValidator.isValid("Ankara", context));
        assertTrue(cityValidator.isValid("Tokyo", context));
        assertTrue(cityValidator.isValid("Mumbai", context));
    }

    @Test
    void testInvalidCities() {
        assertFalse(cityValidator.isValid("New York", context));
        assertFalse(cityValidator.isValid("Paris", context));
        assertFalse(cityValidator.isValid("Beijing", context));
        assertFalse(cityValidator.isValid("Sydney", context));
    }

    @Test
    void testNullCity() {
        assertFalse(cityValidator.isValid(null, context));
    }

}