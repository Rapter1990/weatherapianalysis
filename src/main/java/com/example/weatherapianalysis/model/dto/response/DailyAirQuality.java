package com.example.weatherapianalysis.model.dto.response;

import com.example.weatherapianalysis.utils.CustomLocalDateTimeDeserializer;
import com.example.weatherapianalysis.utils.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyAirQuality {

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime date;

    private List<PollutantCategory> categories;
}
