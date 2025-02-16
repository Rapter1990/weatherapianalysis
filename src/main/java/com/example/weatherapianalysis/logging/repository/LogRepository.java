package com.example.weatherapianalysis.logging.repository;

import com.example.weatherapianalysis.logging.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity,String> {

}
