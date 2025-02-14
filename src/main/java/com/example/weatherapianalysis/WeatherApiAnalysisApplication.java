package com.example.weatherapianalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WeatherApiAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiAnalysisApplication.class, args);
	}

}
