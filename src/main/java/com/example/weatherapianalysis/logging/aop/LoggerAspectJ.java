package com.example.weatherapianalysis.logging.aop;

import com.example.weatherapianalysis.logging.entity.LogEntity;
import com.example.weatherapianalysis.logging.service.LogService;
import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.DailyAirQuality;
import com.example.weatherapianalysis.model.entity.AirQualityRecordEntity;
import com.example.weatherapianalysis.repository.AirQualityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggerAspectJ {

    private final LogService logService;
    private final AirQualityRepository airQualityRepository;
    private final Map<String, Set<LocalDate>> preExecutionRecords = new ConcurrentHashMap<>();

    @Pointcut("execution(* com.example.weatherapianalysis.service.AirQualityService.getAirQualityData(..))")
    public void airQualityServicePointcut() {}

    @Before("airQualityServicePointcut() && args(request)")
    public void capturePreExecutionRecords(AirQualityRequest request) {
        if (request == null || request.getCity() == null) {
            return;
        }
        String city = request.getCity();
        LocalDateTime[] dateRange = getDateRange(request);

        List<AirQualityRecordEntity> existingRecords = airQualityRepository.findByCityAndDateBetween(city, dateRange[0], dateRange[1]);
        Set<LocalDate> dbDates = existingRecords.stream()
                .map(record -> record.getDate().toLocalDate())
                .collect(Collectors.toSet());

        preExecutionRecords.put(city, dbDates);
    }

    @AfterReturning(pointcut = "airQualityServicePointcut()", returning = "response")
    public void logAfterReturning(JoinPoint joinPoint, Object response) {
        if (!(response instanceof AirQualityResponse airQualityResponse)) {
            return;
        }

        String city = airQualityResponse.getCity();
        List<DailyAirQuality> results = airQualityResponse.getResults();
        if (results.isEmpty()) {
            log.info("No data available for city: {}", city);
            return;
        }

        LocalDateTime startDate = results.get(0).getDate();
        LocalDateTime endDate = results.get(results.size() - 1).getDate();

        // Fetch records again after execution
        List<AirQualityRecordEntity> updatedRecords = airQualityRepository.findByCityAndDateBetween(city, startDate, endDate);
        Set<LocalDate> finalDbDates = updatedRecords.stream()
                .map(record -> record.getDate().toLocalDate())
                .collect(Collectors.toSet());

        // Retrieve initially stored database state
        Set<LocalDate> originalDbDates = preExecutionRecords.getOrDefault(city, new HashSet<>());
        preExecutionRecords.remove(city); // Clean up stored data

        List<LocalDate> fromApi = new ArrayList<>();
        List<LocalDate> fromDatabase = new ArrayList<>();

        for (DailyAirQuality record : results) {
            LocalDate recordDate = record.getDate().toLocalDate();
            if (!originalDbDates.contains(recordDate) && finalDbDates.contains(recordDate)) {
                fromApi.add(recordDate);
            } else {
                fromDatabase.add(recordDate);
            }
        }

        // Log API-fetched and database-retrieved data separately
        if (!fromApi.isEmpty()) {
            logData(joinPoint, fromApi, "FROM API", airQualityResponse.toString());
        }
        if (!fromDatabase.isEmpty()) {
            logData(joinPoint, fromDatabase, "FROM DATABASE", airQualityResponse.toString());
        }
    }

    private void logData(JoinPoint joinPoint, List<LocalDate> dates, String source, String response) {
        if (!dates.isEmpty()) {
            String dateRange = dates.get(0) + " to " + dates.get(dates.size() - 1);
            String message = "Data retrieved " + source + " -> " + dateRange;
            log.info(message);
            saveLog(joinPoint, message, "SUCCESS", null, response);
        }
    }

    @AfterThrowing(pointcut = "airQualityServicePointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("Exception in {} with cause = {}", joinPoint.getSignature(), exception.getMessage());
        saveLog(joinPoint, exception.getMessage(), "ERROR", exception.getClass().getName(), null);
    }

    private void saveLog(JoinPoint joinPoint, String message, String status, String errorType, String response) {
        LogEntity logEntity = LogEntity.builder()
                .message(message)
                .endpoint(joinPoint.getSignature().toShortString())
                .method("GET")
                .status(status)
                .errorType(errorType)
                .response(response)
                .operation(joinPoint.getSignature().getName())
                .createdAt(LocalDateTime.now())
                .build();
        logService.saveLogToDatabase(logEntity);
    }

    private LocalDateTime[] getDateRange(AirQualityRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = request.getStartDate() != null ?
                request.getStartDate() :
                now.minusWeeks(1).toLocalDate().atStartOfDay();

        LocalDateTime endDateTime = request.getEndDate() != null ?
                request.getEndDate() :
                now.toLocalDate().atTime(23, 59, 59);

        return new LocalDateTime[]{startDateTime, endDateTime};
    }

}
