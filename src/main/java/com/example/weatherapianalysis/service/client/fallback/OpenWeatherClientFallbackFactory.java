package com.example.weatherapianalysis.service.client.fallback;

import com.example.weatherapianalysis.model.dto.response.OpenWeatherResponse;
import com.example.weatherapianalysis.service.client.OpenWeatherClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenWeatherClientFallbackFactory implements FallbackFactory<OpenWeatherClient> {
    @Override
    public OpenWeatherClient create(Throwable cause) {
        log.error("OpenWeatherClient fallback triggered: {}", cause.getMessage());
        return (latitude, longitude, startTimestamp, endTimestamp, apiKey) -> new OpenWeatherResponse(); // Return empty response
    }
}
