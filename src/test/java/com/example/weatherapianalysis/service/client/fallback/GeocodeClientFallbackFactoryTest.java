package com.example.weatherapianalysis.service.client.fallback;

import com.example.weatherapianalysis.service.client.GeocodeClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeocodeClientFallbackFactoryTest {

    @Test
    void testFallbackReturnsEmptyList() {

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
        GeocodeClientFallbackFactory fallbackFactory = new GeocodeClientFallbackFactory();
        GeocodeClient fallbackClient = fallbackFactory.create(throwable);

        // Verify that the fallback client returns an empty list
        List<?> result = fallbackClient.getCoordinates("TestCity", "dummyApiKey");
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be an empty list");

    }

}