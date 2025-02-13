package com.example.weatherapianalysis.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static String OPEN_WEATHER_API_BASE_URL;
    public static String OPEN_WEATHER_API_KEY;
    public static Integer OPEN_WEATHER_API_CALL_LIMIT;
    public static String OPEN_WEATHER_GEOCODING_URL;
    public static String OPEN_WEATHER_AIR_POLLUTION_URL;

    @Value("${open-weather-api.api-url}")
    private String apiUrlProperty;

    @Value("${open-weather-api.api-key}")
    private String apiKeyProperty;

    @Value("${open-weather-api.api-call-limit}")
    private Integer apiCallLimitProperty;

    @Value("${open-weather-api.geocode-url}")
    private String geocodingUrlProperty;

    @Value("${open-weather-api.air-pollution-url}")
    private String airPollutionUrlProperty;

    @PostConstruct
    public void init() {
        OPEN_WEATHER_API_BASE_URL = apiUrlProperty;
        OPEN_WEATHER_API_KEY = apiKeyProperty;
        OPEN_WEATHER_API_CALL_LIMIT = apiCallLimitProperty;
        OPEN_WEATHER_GEOCODING_URL = geocodingUrlProperty;
        OPEN_WEATHER_AIR_POLLUTION_URL = airPollutionUrlProperty;
    }
}

