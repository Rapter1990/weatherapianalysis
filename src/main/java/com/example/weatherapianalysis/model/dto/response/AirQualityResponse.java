package com.example.weatherapianalysis.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirQualityResponse {
    private String city;
    private List<DailyAirQuality> results;
}
