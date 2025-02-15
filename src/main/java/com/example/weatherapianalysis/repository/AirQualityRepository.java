package com.example.weatherapianalysis.repository;

import com.example.weatherapianalysis.model.entity.AirQualityRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AirQualityRepository extends JpaRepository<AirQualityRecordEntity, String> {

    List<AirQualityRecordEntity> findByCityAndDateBetween(String city, LocalDateTime startDate, LocalDateTime endDate);

}
