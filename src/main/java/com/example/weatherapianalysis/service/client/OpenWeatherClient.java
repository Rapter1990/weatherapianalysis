package com.example.weatherapianalysis.service.client;

import com.example.weatherapianalysis.model.dto.response.OpenWeatherResponse;
import com.example.weatherapianalysis.service.client.fallback.OpenWeatherClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "openWeatherClient",
        url = "${open-weather-api.air-pollution-url}",
        fallbackFactory = OpenWeatherClientFallbackFactory.class
)
public interface OpenWeatherClient {

    @GetMapping
    OpenWeatherResponse getAirPollutionData(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam("start") long startTimestamp,
            @RequestParam("end") long endTimestamp,
            @RequestParam("appid") String apiKey);
}
