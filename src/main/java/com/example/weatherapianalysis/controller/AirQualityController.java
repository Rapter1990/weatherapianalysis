package com.example.weatherapianalysis.controller;

import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.service.AirQualityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AirQualityController {

    private final AirQualityService airQualityService;

    @PostMapping("/airquality")
    public AirQualityResponse getAirQuality(@Valid @RequestBody AirQualityRequest request) {
        return airQualityService.getAirQualityData(request);
    }

}
