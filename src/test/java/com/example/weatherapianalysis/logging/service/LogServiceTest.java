package com.example.weatherapianalysis.logging.service;

import com.example.weatherapianalysis.base.AbstractBaseServiceTest;
import com.example.weatherapianalysis.logging.entity.LogEntity;
import com.example.weatherapianalysis.logging.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

class LogServiceTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LogService logService;

    @Mock
    private LogRepository logRepository;

    @Test
    void testSaveLogToDatabase() {

        // Given
        final LogEntity logEntity = LogEntity.builder()
                .id("1234")
                .message("Test Log")
                .endpoint("/test-endpoint")
                .method("GET")
                .status("200")
                .errorType("None")
                .response("Success")
                .operation("Test Operation")
                .build();

        // When
        when(logRepository.save(any(LogEntity.class))).thenReturn(logEntity);

        // Then
        logService.saveLogToDatabase(logEntity);

        // Verify
        verify(logRepository, times(1)).save(logEntity);

    }

}