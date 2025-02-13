package com.example.weatherapianalysis.service.client;

import com.example.weatherapianalysis.model.dto.response.GeocodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "geocodeClient", url = "${open-weather-api.geocode-url}")
public interface GeocodeClient {

    @GetMapping
    List<GeocodeResponse> getCoordinates(
            @RequestParam("q") String city,
            @RequestParam("appid") String apiKey);
}
