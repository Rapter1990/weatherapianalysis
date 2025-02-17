package com.example.weatherapianalysis.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomAirQualityResponse {
    private String city;
    private List<DailyAirQuality> results;
}
