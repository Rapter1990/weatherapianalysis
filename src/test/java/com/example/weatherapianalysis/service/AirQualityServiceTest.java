package com.example.weatherapianalysis.service;

import com.example.weatherapianalysis.base.AbstractBaseServiceTest;
import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.GeocodeResponse;
import com.example.weatherapianalysis.model.dto.response.OpenWeatherResponse;
import com.example.weatherapianalysis.model.entity.AirQualityRecordEntity;
import com.example.weatherapianalysis.repository.AirQualityRepository;
import com.example.weatherapianalysis.service.client.GeocodeClient;
import com.example.weatherapianalysis.service.client.OpenWeatherClient;
import com.example.weatherapianalysis.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AirQualityServiceTest extends AbstractBaseServiceTest {

    @InjectMocks
    private AirQualityService airQualityService;

    @Mock
    private AirQualityRepository airQualityRepository;

    @Mock
    private OpenWeatherClient openWeatherClient;

    @Mock
    private GeocodeClient geocodeClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(Constants.class, "OPEN_WEATHER_API_KEY", "mock-api-key");
    }

    @Test
    void testGetDateRangeWithProvidedDates() {
        AirQualityRequest request = AirQualityRequest.builder()
                .city("New York")
                .startDate(LocalDateTime.now().minusDays(7))
                .endDate(LocalDateTime.now())
                .build();

        LocalDateTime[] dateRange = ReflectionTestUtils.invokeMethod(airQualityService, "getDateRange", request);

        assertNotNull(dateRange);
        assertEquals(request.getStartDate().toLocalDate(), dateRange[0].toLocalDate());
        assertEquals(request.getEndDate().toLocalDate(), dateRange[1].toLocalDate());
    }

    @Test
    void testGetDateRangeWithoutDates() {
        AirQualityRequest request = AirQualityRequest.builder().city("New York").build();

        LocalDateTime[] dateRange = ReflectionTestUtils.invokeMethod(airQualityService, "getDateRange", request);

        assertNotNull(dateRange);
        assertEquals(LocalDate.now().minusWeeks(1), dateRange[0].toLocalDate());
        assertEquals(LocalDate.now(), dateRange[1].toLocalDate());
    }

    @Test
    void testGetDateRangeWithEndDateOnly() {
        AirQualityRequest request = AirQualityRequest.builder()
                .city("New York")
                .endDate(LocalDateTime.now())
                .build();

        LocalDateTime[] dateRange = ReflectionTestUtils.invokeMethod(airQualityService, "getDateRange", request);

        assertNotNull(dateRange);
        assertEquals(LocalDate.now().minusWeeks(1), dateRange[0].toLocalDate());
        assertEquals(request.getEndDate().toLocalDate(), dateRange[1].toLocalDate());
    }

    @Test
    void testGetDateRangeWithStartDateOnly() {
        AirQualityRequest request = AirQualityRequest.builder()
                .city("New York")
                .startDate(LocalDateTime.now().minusDays(7))
                .build();

        LocalDateTime[] dateRange = ReflectionTestUtils.invokeMethod(airQualityService, "getDateRange", request);

        assertNotNull(dateRange);
        assertEquals(request.getStartDate().toLocalDate(), dateRange[0].toLocalDate());
        assertEquals(LocalDate.now(), dateRange[1].toLocalDate());
    }

    @Test
    void testCreateResponse() {
        String city = "New York";
        List<AirQualityRecordEntity> existingRecords = new ArrayList<>();
        List<AirQualityRecordEntity> newRecords = new ArrayList<>();

        existingRecords.add(AirQualityRecordEntity.builder()
                .city(city)
                .date(LocalDateTime.now().minusDays(2))
                .coLevel(0.4)
                .o3Level(0.5)
                .so2Level(0.6)
                .coCategory("Moderate")
                .o3Category("Unhealthy")
                .so2Category("Hazardous")
                .build());

        newRecords.add(AirQualityRecordEntity.builder()
                .city(city)
                .date(LocalDateTime.now().minusDays(1))
                .coLevel(0.3)
                .o3Level(0.4)
                .so2Level(0.5)
                .coCategory("Good")
                .o3Category("Moderate")
                .so2Category("Unhealthy")
                .build());

        AirQualityResponse response = ReflectionTestUtils.invokeMethod(airQualityService, "createResponse", city, existingRecords, newRecords);

        assertNotNull(response);
        assertEquals(city, response.getCity());
        assertFalse(response.getResults().isEmpty());
        assertEquals(2, response.getResults().size());
    }

    @Test
    void testGetCityCoordinates() {
        String city = "New York";
        GeocodeResponse geocodeResponse = GeocodeResponse.builder()
                .lat(40.7128)
                .lon(-74.0060)
                .build();

        // Ensure mock returns a non-empty list
        when(geocodeClient.getCoordinates(eq(city), anyString())).thenReturn(Collections.singletonList(geocodeResponse));

        GeocodeResponse result = ReflectionTestUtils.invokeMethod(airQualityService, "getCityCoordinates", city);

        assertNotNull(result);
        assertEquals(40.7128, result.getLat());
        assertEquals(-74.0060, result.getLon());
        verify(geocodeClient).getCoordinates(eq(city), anyString());
    }


    @Test
    void testGetCityCoordinatesNotFound() {
        String city = "Unknown City";
        when(geocodeClient.getCoordinates(anyString(), anyString())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () ->
                ReflectionTestUtils.invokeMethod(airQualityService, "getCityCoordinates", city));

        assertEquals("City coordinates not found!", exception.getMessage());
        verify(geocodeClient).getCoordinates(anyString(), anyString());
    }

    @Test
    void testFetchAirQualityData_Success() {
        String city = "New York";
        GeocodeResponse location = GeocodeResponse.builder()
                .lat(40.7128)
                .lon(-74.0060)
                .build();
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        long timestamp = date.toEpochSecond(ZoneOffset.UTC);

        OpenWeatherResponse.Components components = OpenWeatherResponse.Components.builder()
                .co(0.5)
                .o3(0.3)
                .so2(0.2)
                .build();

        OpenWeatherResponse.DataPoint dataPoint = OpenWeatherResponse.DataPoint.builder()
                .components(components)
                .build();

        OpenWeatherResponse apiResponse = OpenWeatherResponse.builder()
                .list(Collections.singletonList(dataPoint))
                .build();

        when(openWeatherClient.getAirPollutionData(eq(40.7128), eq(-74.0060), eq(timestamp), eq(timestamp + 86400), eq("mock-api-key")))
                .thenReturn(apiResponse);

        AirQualityRecordEntity result = ReflectionTestUtils.invokeMethod(airQualityService, "fetchAirQualityData", city, location, date);

        assertNotNull(result);
        assertEquals(0.5, result.getCoLevel());
        assertEquals(0.3, result.getO3Level());
        assertEquals(0.2, result.getSo2Level());
    }

    @Test
    void testFetchAirQualityData_NoData() {
        String city = "New York";
        GeocodeResponse location = GeocodeResponse.builder()
                .lat(40.7128)
                .lon(-74.0060)
                .build();
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        long timestamp = date.toEpochSecond(ZoneOffset.UTC);

        OpenWeatherResponse apiResponse = OpenWeatherResponse.builder()
                .list(Collections.emptyList())
                .build();

        when(openWeatherClient.getAirPollutionData(eq(40.7128), eq(-74.0060), eq(timestamp), eq(timestamp + 86400), eq("mock-api-key")))
                .thenReturn(apiResponse);

        AirQualityRecordEntity result = ReflectionTestUtils.invokeMethod(airQualityService, "fetchAirQualityData", city, location, date);

        assertNull(result);
    }

    @Test
    void testFetchAirQualityData_Exception() {
        String city = "New York";
        GeocodeResponse location = GeocodeResponse.builder()
                .lat(40.7128)
                .lon(-74.0060)
                .build();
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        long timestamp = date.toEpochSecond(ZoneOffset.UTC);

        when(openWeatherClient.getAirPollutionData(eq(40.7128), eq(-74.0060), eq(timestamp), eq(timestamp + 86400), eq("mock-api-key")))
                .thenThrow(new RuntimeException("API error"));

        AirQualityRecordEntity result = ReflectionTestUtils.invokeMethod(airQualityService, "fetchAirQualityData", city, location, date);

        assertNull(result);
    }

    @Test
    void testFetchMissingData_Success() {
        String city = "New York";
        LocalDateTime startDate = LocalDateTime.now().minusDays(5);
        LocalDateTime endDate = LocalDateTime.now();
        Set<LocalDateTime> existingDates = new HashSet<>();

        GeocodeResponse geocodeResponse = GeocodeResponse.builder()
                .lat(40.7128)
                .lon(-74.0060)
                .build();

        when(geocodeClient.getCoordinates(eq(city), anyString())).thenReturn(Collections.singletonList(geocodeResponse));

        when(openWeatherClient.getAirPollutionData(anyDouble(), anyDouble(), anyLong(), anyLong(), anyString()))
                .thenReturn(OpenWeatherResponse.builder()
                        .list(Collections.singletonList(
                                OpenWeatherResponse.DataPoint.builder()
                                        .components(OpenWeatherResponse.Components.builder()
                                                .co(0.5)
                                                .o3(0.3)
                                                .so2(0.2)
                                                .build())
                                        .build()))
                        .build());

        List<AirQualityRecordEntity> result = ReflectionTestUtils.invokeMethod(
                airQualityService, "fetchMissingData", city, startDate, endDate, existingDates);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(geocodeClient).getCoordinates(eq(city), anyString());
        verify(openWeatherClient, atLeastOnce()).getAirPollutionData(anyDouble(), anyDouble(), anyLong(), anyLong(), anyString());
    }

    @Test
    void testFetchMissingData_NoNewData() {
        String city = "New York";
        LocalDateTime startDate = LocalDateTime.now().minusDays(5);
        LocalDateTime endDate = LocalDateTime.now();
        Set<LocalDateTime> existingDates = new HashSet<>(List.of(startDate, endDate));

        GeocodeResponse geocodeResponse = GeocodeResponse.builder()
                .lat(40.7128)
                .lon(-74.0060)
                .build();

        when(geocodeClient.getCoordinates(eq(city), anyString())).thenReturn(Collections.singletonList(geocodeResponse));

        List<AirQualityRecordEntity> result = ReflectionTestUtils.invokeMethod(
                airQualityService, "fetchMissingData", city, startDate, endDate, existingDates);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(geocodeClient).getCoordinates(eq(city), anyString());

    }

    @Test
    void testFetchMissingData_Exception() {
        String city = "New York";
        LocalDateTime startDate = LocalDateTime.now().minusDays(5);
        LocalDateTime endDate = LocalDateTime.now();
        Set<LocalDateTime> existingDates = new HashSet<>();

        when(geocodeClient.getCoordinates(eq(city), anyString())).thenThrow(new RuntimeException("API error"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                ReflectionTestUtils.invokeMethod(airQualityService, "fetchMissingData", city, startDate, endDate, existingDates));

        assertEquals("API error", exception.getMessage());
        verify(geocodeClient).getCoordinates(eq(city), anyString());
    }

    @Test
    void testGetAirQualityData() {
        AirQualityRequest request = AirQualityRequest.builder()
                .city("New York")
                .startDate(LocalDateTime.now().minusDays(5))
                .endDate(LocalDateTime.now())
                .build();

        List<AirQualityRecordEntity> existingRecords = new ArrayList<>();
        when(airQualityRepository.findByCityAndDateBetween(anyString(), any(), any())).thenReturn(existingRecords);

        GeocodeResponse geocodeResponse = GeocodeResponse.builder()
                .lat(40.7128)
                .lon(-74.0060)
                .build();
        when(geocodeClient.getCoordinates(anyString(), anyString())).thenReturn(Collections.singletonList(geocodeResponse));

        when(openWeatherClient.getAirPollutionData(anyDouble(), anyDouble(), anyLong(), anyLong(), anyString()))
                .thenReturn(OpenWeatherResponse.builder()
                        .list(Collections.singletonList(
                                OpenWeatherResponse.DataPoint.builder()
                                        .components(OpenWeatherResponse.Components.builder()
                                                .co(0.5)
                                                .o3(0.3)
                                                .so2(0.2)
                                                .build())
                                        .build()))
                        .build());

        AirQualityResponse response = airQualityService.getAirQualityData(request);

        assertNotNull(response);
        assertEquals("New York", response.getCity());
        verify(airQualityRepository).findByCityAndDateBetween(anyString(), any(), any());
        verify(geocodeClient).getCoordinates(anyString(), anyString());
        verify(openWeatherClient, atLeastOnce()).getAirPollutionData(anyDouble(), anyDouble(), anyLong(), anyLong(), anyString());
    }


}