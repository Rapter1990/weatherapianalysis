package com.example.weatherapianalysis.controller;

import com.example.weatherapianalysis.base.AbstractRestControllerTest;
import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.DailyAirQuality;
import com.example.weatherapianalysis.model.dto.response.PollutantCategory;
import com.example.weatherapianalysis.service.AirQualityService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class AirQualityControllerTest extends AbstractRestControllerTest {

    @MockitoBean
    AirQualityService airQualityService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    void whenValidRequest_thenReturnAirQualityResponse() throws Exception {

        // Given
        AirQualityRequest request = AirQualityRequest.builder()
                .city("Ankara")
                .startDate(LocalDate.parse("07-02-2025", FORMATTER).atStartOfDay()) // Correct format
                .endDate(LocalDate.parse("20-02-2025", FORMATTER).atStartOfDay()) // Correct format
                .build();

        AirQualityResponse response = AirQualityResponse.builder()
                .city("Ankara")
                .results(List.of(
                        DailyAirQuality.builder()
                                .date(LocalDate.parse("07-02-2025", FORMATTER).atStartOfDay())
                                .categories(List.of(
                                        new PollutantCategory("PM2.5", "Moderate"),
                                        new PollutantCategory("NO2", "Unhealthy")
                                ))
                                .build(),
                        DailyAirQuality.builder()
                                .date(LocalDate.parse("20-02-2025", FORMATTER).atStartOfDay())
                                .categories(List.of(
                                        new PollutantCategory("PM10", "Good")
                                ))
                                .build()
                ))
                .build();

        // When
        when(airQualityService.getAirQualityData(any(AirQualityRequest.class)))
                .thenReturn(response);

        // Then
        mockMvc.perform(post("/api/v1/airquality")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Ankara"))
                .andExpect(jsonPath("$.results[0].date").value("07-02-2025"))
                .andExpect(jsonPath("$.results[0].categories[0].pollutant").value("PM2.5"))
                .andExpect(jsonPath("$.results[0].categories[0].category").value("Moderate"))
                .andExpect(jsonPath("$.results[0].categories[1].pollutant").value("NO2"))
                .andExpect(jsonPath("$.results[0].categories[1].category").value("Unhealthy"))
                .andExpect(jsonPath("$.results[1].date").value("20-02-2025"))
                .andExpect(jsonPath("$.results[1].categories[0].pollutant").value("PM10"))
                .andExpect(jsonPath("$.results[1].categories[0].category").value("Good"));

        // Verify
        verify(airQualityService, times(1)).getAirQualityData(any(AirQualityRequest.class));
    }

    @Test
    void whenInvalidCity_thenReturnBadRequest() throws Exception {
        // Arrange
        AirQualityRequest invalidRequest = AirQualityRequest.builder()
                .city("New York") // Invalid city (not in allowed list)
                .startDate(LocalDate.parse("07-02-2025", FORMATTER).atStartOfDay())
                .endDate(LocalDate.parse("20-02-2025", FORMATTER).atStartOfDay())
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/airquality")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors[0].message").value("City must be one of: London, Barcelona, Ankara, Tokyo, Mumbai"))
                .andExpect(jsonPath("$.subErrors[0].field").value("city")); // Ensure correct field validation

        verifyNoInteractions(airQualityService);
    }

    @Test
    void whenMissingCity_thenReturnBadRequest() throws Exception {
        // Arrange
        AirQualityRequest invalidRequest = AirQualityRequest.builder()
                .startDate(LocalDate.parse("07-02-2025", FORMATTER).atStartOfDay())
                .endDate(LocalDate.parse("20-02-2025", FORMATTER).atStartOfDay())
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/airquality")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors[*].message").value(hasItems(
                        "City name is required",
                        "City must be one of: London, Barcelona, Ankara, Tokyo, Mumbai"
                )));

        verifyNoInteractions(airQualityService);
    }

}