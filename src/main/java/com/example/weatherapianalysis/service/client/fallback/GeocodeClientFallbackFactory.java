package com.example.weatherapianalysis.service.client.fallback;

import com.example.weatherapianalysis.service.client.GeocodeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class GeocodeClientFallbackFactory implements FallbackFactory<GeocodeClient> {

    @Override
    public GeocodeClient create(Throwable cause) {
        log.error("GeocodeClient fallback triggered: {}", cause.getMessage());
        return (city, apiKey) -> Collections.emptyList(); // Return empty list as fallback
    }

}
