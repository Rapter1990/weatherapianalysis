package com.example.weatherapianalysis.utils;

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

    @Value("${open-weather-api.api-url}")
    public void setGeocodingUrl(String apiUrl) {
        OPEN_WEATHER_GEOCODING_URL = apiUrl + "/geo/1.0/direct";
    }

    @Value("${open-weather-api.api-url}")
    public void setAirPollutionUrl(String apiUrl) {
        OPEN_WEATHER_AIR_POLLUTION_URL = apiUrl + "/data/2.5/air_pollution/history";
    }

}
