package com.example.weatherapianalysis.utils.validator;

import com.example.weatherapianalysis.utils.annotation.ValidCity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CityValidator implements ConstraintValidator<ValidCity, String> {

    private static final Set<String> ALLOWED_CITIES =
            Set.of("London", "Barcelona", "Ankara", "Tokyo", "Mumbai");

    @Override
    public boolean isValid(String city, ConstraintValidatorContext constraintValidatorContext) {
        return city != null && ALLOWED_CITIES.contains(city);
    }

}
