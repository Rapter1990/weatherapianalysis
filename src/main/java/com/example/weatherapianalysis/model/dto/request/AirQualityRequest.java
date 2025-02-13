package com.example.weatherapianalysis.model.dto.request;

import com.example.weatherapianalysis.utils.annotation.ValidCity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AirQualityRequest {

    @NotBlank(message = "City name is required")
    @ValidCity
    private String city;

    @DateTimeFormat(pattern = "dd-MM-yyyy", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

}
