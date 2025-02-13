package com.example.weatherapianalysis.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeocodeResponse {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
