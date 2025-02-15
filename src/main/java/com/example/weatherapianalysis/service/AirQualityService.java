package com.example.weatherapianalysis.service;

import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.*;
import com.example.weatherapianalysis.model.entity.AirQualityRecordEntity;
import com.example.weatherapianalysis.repository.AirQualityRepository;
import com.example.weatherapianalysis.service.client.GeocodeClient;
import com.example.weatherapianalysis.service.client.OpenWeatherClient;
import com.example.weatherapianalysis.utils.AQIClassifier;
import com.example.weatherapianalysis.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirQualityService {

    private final AirQualityRepository airQualityRepository;
    private final OpenWeatherClient openWeatherClient;
    private final GeocodeClient geocodeClient;

    public AirQualityResponse getAirQualityData(AirQualityRequest request) {
        LocalDateTime[] dateRange = getDateRange(request);
        String city = request.getCity();

        List<AirQualityRecordEntity> existingRecords = airQualityRepository.findByCityAndDateBetween(city, dateRange[0], dateRange[1]);
        Set<LocalDateTime> existingDates = existingRecords.stream().map(AirQualityRecordEntity::getDate).collect(Collectors.toSet());

        List<AirQualityRecordEntity> newRecords = fetchMissingData(city, dateRange[0], dateRange[1], existingDates);
        airQualityRepository.saveAll(newRecords);

        return createResponse(city, existingRecords, newRecords);
    }

    private LocalDateTime[] getDateRange(AirQualityRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = request.getStartDate() != null ?
                request.getStartDate() :
                now.minusWeeks(1).toLocalDate().atStartOfDay(); // Last week's start

        LocalDateTime endDateTime = request.getEndDate() != null ?
                request.getEndDate() :
                now.toLocalDate().atTime(23, 59, 59); // Today end time

        return new LocalDateTime[]{startDateTime, endDateTime};
    }

    private List<AirQualityRecordEntity> fetchMissingData(String city, LocalDateTime startDate, LocalDateTime endDate, Set<LocalDateTime> existingDates) {
        List<AirQualityRecordEntity> newRecords = new ArrayList<>();
        GeocodeResponse location = getCityCoordinates(city);

        for (LocalDateTime date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!existingDates.contains(date)) {
                AirQualityRecordEntity newRecord = fetchAirQualityData(city, location, date);
                if (newRecord != null) {
                    newRecords.add(newRecord);
                }
            } else {
                log.info("Fetched data from DB for city: {}, date: {}", city, date);
            }
        }
        return newRecords;
    }

    private GeocodeResponse getCityCoordinates(String city) {
        List<GeocodeResponse> geoResponses = geocodeClient.getCoordinates(city, Constants.OPEN_WEATHER_API_KEY);
        if (geoResponses.isEmpty()) {
            throw new RuntimeException("City coordinates not found!");
        }
        return geoResponses.get(0);
    }

    private AirQualityRecordEntity fetchAirQualityData(String city, GeocodeResponse location, LocalDateTime date) {
        try {
            long timestamp = date.toEpochSecond(ZoneOffset.UTC);
            OpenWeatherResponse apiResponse = openWeatherClient.getAirPollutionData(
                    location.getLat(), location.getLon(), timestamp, timestamp + 86400, Constants.OPEN_WEATHER_API_KEY);

            if (apiResponse.getList() == null || apiResponse.getList().isEmpty()) {
                log.warn("No data found for city: {}, date: {}", city, date);
                return null;
            }

            OpenWeatherResponse.DataPoint data = apiResponse.getList().get(0);
            OpenWeatherResponse.Components components = data.getComponents();

            return AirQualityRecordEntity.builder()
                    .city(city)
                    .date(date)
                    .coLevel(components.getCo())
                    .o3Level(components.getO3())
                    .so2Level(components.getSo2())
                    .coCategory(AQIClassifier.classifyCO(components.getCo()).toString())
                    .o3Category(AQIClassifier.classifyO3(components.getO3()).toString())
                    .so2Category(AQIClassifier.classifySO2(components.getSo2()).toString())
                    .build();

        } catch (Exception e) {
            log.error("Error fetching air quality data for city: {}, date: {}", city, date, e);
            return null;
        }
    }

    private AirQualityResponse createResponse(String city, List<AirQualityRecordEntity> existingRecords, List<AirQualityRecordEntity> newRecords) {
        List<AirQualityRecordEntity> allRecords = new ArrayList<>(existingRecords);
        allRecords.addAll(newRecords);

        List<DailyAirQuality> dailyResults = allRecords.stream().map(record ->
                new DailyAirQuality(
                        record.getDate(),
                        List.of(
                                new PollutantCategory("CO", record.getCoCategory()),
                                new PollutantCategory("O3", record.getO3Category()),
                                new PollutantCategory("SO2", record.getSo2Category())
                        )
                )).collect(Collectors.toList());

        return AirQualityResponse.builder()
                .city(city)
                .results(dailyResults)
                .build();

    }

}
