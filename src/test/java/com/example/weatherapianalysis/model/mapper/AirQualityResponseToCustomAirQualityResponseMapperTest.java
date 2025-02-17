package com.example.weatherapianalysis.model.mapper;

import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.CustomAirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.DailyAirQuality;
import com.example.weatherapianalysis.model.dto.response.PollutantCategory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirQualityResponseToCustomAirQualityResponseMapperTest {

    private final AirQualityResponseToCustomAirQualityResponseMapper mapper = Mappers.getMapper(AirQualityResponseToCustomAirQualityResponseMapper.class);

    @Test
    void testMapAirQualityResponseNull() {
        CustomAirQualityResponse result = mapper.map((AirQualityResponse) null);
        assertNull(result);
    }

    @Test
    void testMapAirQualityResponseListNull() {
        List<CustomAirQualityResponse> result = mapper.map((List<AirQualityResponse>) null);
        assertNull(result);
    }

    @Test
    void testMapAirQualityResponseListEmpty() {
        List<CustomAirQualityResponse> result = mapper.map(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMapAirQualityResponseListWithNullElements() {
        AirQualityResponse response1 = AirQualityResponse.builder()
                .city("Ankara")
                .results(Collections.emptyList())
                .build();
        AirQualityResponse response2 = null;

        List<AirQualityResponse> responses = Arrays.asList(response1, response2);
        List<CustomAirQualityResponse> result = mapper.map(responses);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNull(result.get(1));
    }

    @Test
    void testMapAirQualityResponseWithValidValues() {
        AirQualityResponse response = AirQualityResponse.builder()
                .city("London")
                .results(Arrays.asList(
                        DailyAirQuality.builder()
                                .date(LocalDateTime.now())
                                .categories(Arrays.asList(
                                        new PollutantCategory("PM2.5", "Moderate"),
                                        new PollutantCategory("NO2", "Unhealthy")
                                ))
                                .build()
                ))
                .build();

        CustomAirQualityResponse result = mapper.map(response);
        assertNotNull(result);
        assertEquals(response.getCity(), result.getCity());
        assertEquals(response.getResults().size(), result.getResults().size());
    }

    @Test
    void testMapAirQualityResponseListWithValidValues() {
        AirQualityResponse response1 = AirQualityResponse.builder()
                .city("Tokyo")
                .results(Collections.emptyList())
                .build();
        AirQualityResponse response2 = AirQualityResponse.builder()
                .city("Mumbai")
                .results(Collections.emptyList())
                .build();

        List<AirQualityResponse> responses = Arrays.asList(response1, response2);
        List<CustomAirQualityResponse> result = mapper.map(responses);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1.getCity(), result.get(0).getCity());
        assertEquals(response2.getCity(), result.get(1).getCity());
    }

    @Test
    void testMapAirQualityResponseWithNullValues() {
        AirQualityResponse response = AirQualityResponse.builder()
                .city(null)
                .results(null)
                .build();

        CustomAirQualityResponse result = mapper.map(response);

        assertNotNull(result);
        assertNull(result.getCity());
        assertNull(result.getResults());
    }

    @Test
    void testMapAirQualityResponseListWithEdgeCaseValues() {
        AirQualityResponse response = AirQualityResponse.builder()
                .city("")
                .results(Collections.emptyList())
                .build();

        CustomAirQualityResponse result = mapper.map(response);
        assertNotNull(result);
        assertEquals("", result.getCity());
        assertTrue(result.getResults().isEmpty());
    }

}