package com.example.weatherapianalysis.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollutantCategory {
    private String pollutant;
    private String category;
}
