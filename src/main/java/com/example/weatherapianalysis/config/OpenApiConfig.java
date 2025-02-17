package com.example.weatherapianalysis.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Sercan Noyan Germiyanoğlu",
                        url = "https://github.com/Rapter1990/weatherapianalysis"
                ),
                description = "Case Study - Weather Api Analysis" +
                        " (Java 21, Spring Boot, Postgres, JUnit, Jacoco, Sonarqube, Docker, Kubernetes, Prometheus, Grafana, Github Actions (CI/CD), Jenkins ) ",
                title = "weatherapianalysis",
                version = "1.0.0"
        )
)
public class OpenApiConfig {

}
