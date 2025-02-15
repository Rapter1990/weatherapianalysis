package com.example.weatherapianalysis.service.client.fallback;

import com.example.weatherapianalysis.model.dto.response.OpenWeatherResponse;
import com.example.weatherapianalysis.service.client.OpenWeatherClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenWeatherClientFallbackFactoryTest {

    @Test
    void testFallbackReturnsEmptyResponse() {
        // Simulate an exception that triggers the fallback
        Request request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost",
                Collections.emptyMap(),
                null,
                StandardCharsets.UTF_8,
                null
        );

        Throwable throwable = new FeignException.ServiceUnavailable("Service Unavailable", request, null, null);

        // Create the fallback factory
        OpenWeatherClientFallbackFactory fallbackFactory = new OpenWeatherClientFallbackFactory();
        OpenWeatherClient fallbackClient = fallbackFactory.create(throwable);

        // Verify that the fallback client returns an empty response
        OpenWeatherResponse response = fallbackClient.getAirPollutionData(12.34, 56.78, 1618309200, 1618395600, "dummyApiKey");
        assertNotNull(response, "Response should not be null");
    }

}