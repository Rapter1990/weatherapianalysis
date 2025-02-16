package com.example.weatherapianalysis.logging.entity;

import com.example.weatherapianalysis.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "air_quality_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class LogEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String message;

    private String endpoint;

    private String method;

    private String status;

    private String errorType;

    private String response;

    private String operation;

}
