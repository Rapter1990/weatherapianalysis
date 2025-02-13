package com.example.weatherapianalysis.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static String OPEN_WEATHER_API_BASE_URL;
    public static String OPEN_WEATHER_API_KEY;

    public static Integer OPEN_WEATHER_API_CALL_LIMIT;

    @Value("${open-weather-api.api-url}")
    public void setOpenWeatherApiBaseUrl(String apiUrl) {
        OPEN_WEATHER_API_BASE_URL = apiUrl;
    }

    @Value("${open-weather-api.api-key}")
    public void setOpenWeatherApiKey(String apiKey) {
        OPEN_WEATHER_API_KEY = apiKey;
    }

    @Value("${exchange-api.api-call-limit}")
    public void setOpenWeatherApiCallLimit(Integer apiCallLimit) {
        OPEN_WEATHER_API_CALL_LIMIT = apiCallLimit;
    }

}
