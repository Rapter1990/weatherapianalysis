package com.example.weatherapianalysis.controller;

import com.example.weatherapianalysis.model.dto.request.AirQualityRequest;
import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.CustomAirQualityResponse;
import com.example.weatherapianalysis.model.mapper.AirQualityResponseToCustomAirQualityResponseMapper;
import com.example.weatherapianalysis.service.AirQualityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Air Quality Management",
        description = "Endpoints for retrieving air quality data.")
public class AirQualityController {

    private final AirQualityService airQualityService;

    private final AirQualityResponseToCustomAirQualityResponseMapper airQualityResponseToCustomAirQualityResponseMapper =
            AirQualityResponseToCustomAirQualityResponseMapper.initialize();


    @Operation(
            summary = "Retrieve air quality data",
            description = "Fetches air quality information based on the provided request parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved air quality data"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Validation failed")
            }
    )
    @PostMapping("/airquality")
    public ResponseEntity<CustomAirQualityResponse> getAirQuality(@Valid @RequestBody AirQualityRequest request) {

        final AirQualityResponse airQualityResponse = airQualityService.getAirQualityData(request);
        final CustomAirQualityResponse customAirQualityResponse = airQualityResponseToCustomAirQualityResponseMapper.map(airQualityResponse);
        return ResponseEntity.ok(customAirQualityResponse);
    }

}
