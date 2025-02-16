package com.example.weatherapianalysis.logging.service;

import com.example.weatherapianalysis.logging.entity.LogEntity;
import com.example.weatherapianalysis.logging.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void saveLogToDatabase(final LogEntity logEntity) {
        logRepository.save(logEntity);
    }
}
