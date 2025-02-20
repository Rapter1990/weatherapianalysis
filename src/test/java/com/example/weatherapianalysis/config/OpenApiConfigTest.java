package com.example.weatherapianalysis.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenApiConfigTest {

    @Test
    void openApiInfo() {

        // Given
        OpenAPIDefinition openAPIDefinition = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class);

        // Then
        assertEquals("1.0.0", openAPIDefinition.info().version());
        assertEquals("weatherapianalysis", openAPIDefinition.info().title());
        assertEquals("Case Study - Weather Api Analysis" +
                        " (Java 21, Spring Boot, Postgres, JUnit, Jacoco, Sonarqube, Docker, Kubernetes, Prometheus, Grafana, Github Actions (CI/CD), Jenkins ) ",
                openAPIDefinition.info().description());

    }

    @Test
    void contactInfo() {

        // Given
        Info info = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class).info();
        Contact contact = info.contact();

        // Then
        assertEquals("Sercan Noyan Germiyanoğlu", contact.name());
        assertEquals("https://github.com/Rapter1990/weatherapianalysis", contact.url());

    }

}