package com.example.weatherapianalysis.logging.aop;

import com.example.weatherapianalysis.base.AbstractBaseServiceTest;
import com.example.weatherapianalysis.logging.entity.LogEntity;
import com.example.weatherapianalysis.logging.service.LogService;
import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.DailyAirQuality;
import com.example.weatherapianalysis.model.dto.response.PollutantCategory;
import com.example.weatherapianalysis.model.entity.AirQualityRecordEntity;
import com.example.weatherapianalysis.repository.AirQualityRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoggerAspectJTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LoggerAspectJ loggerAspectJ;

    @Mock
    private LogService logService;

    @Mock
    private AirQualityRepository airQualityRepository;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    private AirQualityRequest airQualityRequest;

    @BeforeEach
    void setUp() {
        airQualityRequest = new AirQualityRequest();
        airQualityRequest.setCity("London");
        airQualityRequest.setStartDate(LocalDateTime.now().minusDays(7));
        airQualityRequest.setEndDate(LocalDateTime.now());

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(servletRequestAttributes.getResponse()).thenReturn(httpServletResponse);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(signature.getDeclaringTypeName()).thenReturn("LoggerAspectJ");
        when(signature.getDeclaringType()).thenReturn(LoggerAspectJ.class);
    }

    @Test
    void testGetDateRange_WithProvidedDates() {
        // Given
        airQualityRequest.setStartDate(LocalDateTime.now().minusDays(7));
        airQualityRequest.setEndDate(LocalDateTime.now());

        // When
        LocalDateTime[] dateRange = (LocalDateTime[]) ReflectionTestUtils.invokeMethod(loggerAspectJ, "getDateRange", airQualityRequest);

        // Then
        assertNotNull(dateRange);
        assertEquals(airQualityRequest.getStartDate().toLocalDate(), dateRange[0].toLocalDate());
        assertEquals(airQualityRequest.getEndDate().toLocalDate(), dateRange[1].toLocalDate());
    }

    @Test
    void testGetDateRange_WithoutDates() {
        // Given
        airQualityRequest.setStartDate(null);
        airQualityRequest.setEndDate(null);

        // When
        LocalDateTime[] dateRange = (LocalDateTime[]) ReflectionTestUtils.invokeMethod(loggerAspectJ, "getDateRange", airQualityRequest);

        // Then
        assertNotNull(dateRange);
        assertEquals(LocalDate.now().minusWeeks(1), dateRange[0].toLocalDate());
        assertEquals(LocalDate.now(), dateRange[1].toLocalDate());
    }

    @Test
    void testGetDateRange_WithEndDateOnly() {
        // Given
        airQualityRequest.setStartDate(null);
        airQualityRequest.setEndDate(LocalDateTime.now());

        // When
        LocalDateTime[] dateRange = (LocalDateTime[]) ReflectionTestUtils.invokeMethod(loggerAspectJ, "getDateRange", airQualityRequest);

        // Then
        assertNotNull(dateRange);
        assertEquals(LocalDate.now().minusWeeks(1), dateRange[0].toLocalDate());
        assertEquals(airQualityRequest.getEndDate().toLocalDate(), dateRange[1].toLocalDate());
    }

    @Test
    void testGetDateRange_WithStartDateOnly() {
        // Given
        airQualityRequest.setStartDate(LocalDateTime.now().minusDays(7));
        airQualityRequest.setEndDate(null);

        // When
        LocalDateTime[] dateRange = (LocalDateTime[]) ReflectionTestUtils.invokeMethod(loggerAspectJ, "getDateRange", airQualityRequest);

        // Then
        assertNotNull(dateRange);
        assertEquals(airQualityRequest.getStartDate().toLocalDate(), dateRange[0].toLocalDate());
        assertEquals(LocalDate.now(), dateRange[1].toLocalDate());
    }

    @Test
    void testSaveLog_ShouldInvokeLogService() {
        // Given
        String message = "Test Log Message";
        String status = "SUCCESS";
        String response = "Test Response";
        when(joinPoint.getSignature().toShortString()).thenReturn("execution(LogService.saveLogToDatabase(..))");
        when(joinPoint.getSignature().getName()).thenReturn("saveLogToDatabase");

        // When
        ReflectionTestUtils.invokeMethod(loggerAspectJ, "saveLog", joinPoint, message, status, null, response);

        // Then
        verify(logService, times(1)).saveLogToDatabase(argThat(log ->
                log.getMessage().equals(message) &&
                        log.getStatus().equals(status) &&
                        log.getMethod().equals("GET") &&
                        log.getEndpoint().equals("execution(LogService.saveLogToDatabase(..))") &&
                        log.getOperation().equals("saveLogToDatabase") &&
                        log.getResponse().equals(response)
        ));
    }

    @Test
    void testLogData_WithValidDates_ShouldInvokeSaveLog() {
        // Given
        List<LocalDate> dates = Arrays.asList(LocalDate.now().minusDays(1), LocalDate.now());
        String response = "Sample Response";

        // When
        ReflectionTestUtils.invokeMethod(loggerAspectJ, "logData", joinPoint, dates, "FROM API", response);

        // Then
        verify(logService, times(1)).saveLogToDatabase(argThat(log -> log.getResponse().equals(response)));
    }

    @Test
    void testLogData_WithEmptyDates_ShouldNotInvokeSaveLog() {
        // Given
        List<LocalDate> dates = Collections.emptyList();
        String response = "No Data Response";

        // When
        ReflectionTestUtils.invokeMethod(loggerAspectJ, "logData", joinPoint, dates, "FROM API", response);

        // Then
        verify(logService, never()).saveLogToDatabase(any(LogEntity.class));
    }

    @Test
    void testLogData_WithSingleDate_ShouldInvokeSaveLog() {
        // Given
        List<LocalDate> dates = Collections.singletonList(LocalDate.now());
        String response = "Single Date Response";

        // When
        ReflectionTestUtils.invokeMethod(loggerAspectJ, "logData", joinPoint, dates, "FROM API", response);

        // Then
        verify(logService, times(1)).saveLogToDatabase(argThat(log -> log.getResponse().equals(response)));
    }

    @Test
    void testLogAfterThrowing_ShouldLogException() {
        // Given
        Exception exception = new RuntimeException("Test Exception");
        String response = null;

        // When
        loggerAspectJ.logAfterThrowing(joinPoint, exception);

        // Then
        verify(logService, times(1)).saveLogToDatabase(argThat(log -> log.getResponse() == response));
    }

    @Test
    void testLogAfterReturning_WithValidResponse_ShouldLogData_FromAPI() {
        // Given
        LocalDateTime dateTime = LocalDateTime.now();
        DailyAirQuality dailyAirQuality = DailyAirQuality.builder()
                .date(dateTime)
                .categories(Arrays.asList(
                        new PollutantCategory("CO", "Moderate"),
                        new PollutantCategory("NO2", "Unhealthy")))
                .build();

        AirQualityResponse response = new AirQualityResponse();
        response.setCity("London");
        response.setResults(Collections.singletonList(dailyAirQuality));

        when(airQualityRepository.findByCityAndDateBetween(anyString(), any(), any()))
                .thenReturn(Collections.emptyList());

        // When
        loggerAspectJ.logAfterReturning(joinPoint, response);

        // Then
        verify(logService, times(1)).saveLogToDatabase(any(LogEntity.class));
    }

    @Test
    void testLogAfterReturning_WithValidResponse_ShouldLogData_FromDatabase() {
        // Given
        LocalDateTime dateTime = LocalDateTime.now();
        DailyAirQuality dailyAirQuality = DailyAirQuality.builder()
                .date(dateTime)
                .categories(Arrays.asList(
                        new PollutantCategory("CO", "Moderate"),
                        new PollutantCategory("NO2", "Unhealthy")))
                .build();

        AirQualityResponse response = new AirQualityResponse();
        response.setCity("London");
        response.setResults(Collections.singletonList(dailyAirQuality));

        AirQualityRecordEntity recordEntity = AirQualityRecordEntity.builder()
                .date(dateTime)
                .city("London")
                .coLevel(4.5)
                .o3Level(120.0)
                .so2Level(30.0)
                .coCategory("Moderate")
                .o3Category("Unhealthy")
                .so2Category("Good")
                .build();

        when(airQualityRepository.findByCityAndDateBetween(anyString(), any(), any()))
                .thenReturn(List.of(recordEntity));

        // When
        loggerAspectJ.logAfterReturning(joinPoint, response);

        // Then
        verify(logService, times(1)).saveLogToDatabase(any(LogEntity.class));
    }

    @Test
    void testCapturePreExecutionRecords_WhenCityIsNull_ShouldNotStoreRecords() {
        // Given
        airQualityRequest.setCity(null);

        // When
        loggerAspectJ.capturePreExecutionRecords(airQualityRequest);

        // Then
        verifyNoInteractions(airQualityRepository);
    }

    @Test
    void testCapturePreExecutionRecords_ShouldStoreRecords() {
        // Given
        List<AirQualityRecordEntity> existingRecords = List.of(
                AirQualityRecordEntity.builder()
                        .date(LocalDateTime.now().minusDays(1))
                        .city("London")
                        .build()
        );

        when(airQualityRepository.findByCityAndDateBetween(anyString(), any(), any())).thenReturn(existingRecords);

        // When
        loggerAspectJ.capturePreExecutionRecords(airQualityRequest);

        // Then
        verify(airQualityRepository, times(1)).findByCityAndDateBetween(anyString(), any(), any());
    }

}