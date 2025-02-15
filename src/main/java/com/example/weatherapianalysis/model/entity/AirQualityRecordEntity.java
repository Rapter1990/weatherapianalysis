package com.example.weatherapianalysis.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "air_quality_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class AirQualityRecordEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String city;

    private LocalDateTime date;

    private double coLevel;
    private double o3Level;
    private double so2Level;

    private String coCategory;
    private String o3Category;
    private String so2Category;

}
