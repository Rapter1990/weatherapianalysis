package com.example.weatherapianalysis.controller;

import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.CustomAirQualityResponse;
import com.example.weatherapianalysis.model.mapper.AirQualityResponseToCustomAirQualityResponseMapper;
import com.example.weatherapianalysis.service.AirQualityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AirQualityController {

    private final AirQualityService airQualityService;

    private final AirQualityResponseToCustomAirQualityResponseMapper airQualityResponseToCustomAirQualityResponseMapper =
            AirQualityResponseToCustomAirQualityResponseMapper.initialize();

    @PostMapping("/airquality")
    public ResponseEntity<CustomAirQualityResponse> getAirQuality(@Valid @RequestBody AirQualityRequest request) {

        final AirQualityResponse airQualityResponse = airQualityService.getAirQualityData(request);
        final CustomAirQualityResponse customAirQualityResponse = airQualityResponseToCustomAirQualityResponseMapper.map(airQualityResponse);
        return ResponseEntity.ok(customAirQualityResponse);
    }

}
