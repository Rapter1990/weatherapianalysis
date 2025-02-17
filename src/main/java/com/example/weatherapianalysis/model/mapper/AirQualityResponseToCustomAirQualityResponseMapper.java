package com.example.weatherapianalysis.model.mapper;

import com.example.weatherapianalysis.model.dto.response.AirQualityResponse;
import com.example.weatherapianalysis.model.dto.response.CustomAirQualityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirQualityResponseToCustomAirQualityResponseMapper extends BaseMapper<AirQualityResponse, CustomAirQualityResponse> {

    static AirQualityResponseToCustomAirQualityResponseMapper initialize() {
        return Mappers.getMapper(AirQualityResponseToCustomAirQualityResponseMapper.class);
    }

}
