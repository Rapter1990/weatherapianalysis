package com.example.weatherapianalysis.utils.annotation;

import com.example.weatherapianalysis.utils.validator.CityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CityValidator.class)
public @interface ValidCity {
    String message() default "City must be one of: London, Barcelona, Ankara, Tokyo, Mumbai";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
