package com.example.weatherapianalysis.model.dto.request;

import com.example.weatherapianalysis.utils.CustomLocalDateTimeDeserializer;
import com.example.weatherapianalysis.utils.CustomLocalDateTimeSerializer;
import com.example.weatherapianalysis.utils.annotation.ValidCity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirQualityRequest {

    @NotBlank(message = "City name is required")
    @ValidCity
    private String city;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime startDate;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDate;

}
