package com.example.weatherapianalysis.model.dto.request;

import com.example.weatherapianalysis.utils.CustomLocalDateTimeDeserializer;
import com.example.weatherapianalysis.utils.annotation.ValidCity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AirQualityRequest {

    @NotBlank(message = "City name is required")
    @ValidCity
    private String city;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

}
