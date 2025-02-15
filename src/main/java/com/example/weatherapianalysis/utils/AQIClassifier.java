package com.example.weatherapianalysis.utils;

import com.example.weatherapianalysis.model.enums.AQICategory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AQIClassifier {

    public AQICategory classifyCO(double concentration) {
        if (concentration <= 4400) return AQICategory.GOOD;
        if (concentration <= 9400) return AQICategory.SATISFACTORY;
        if (concentration <= 12400) return AQICategory.MODERATE;
        if (concentration <= 15400) return AQICategory.POOR;
        if (concentration <= 30400) return AQICategory.SEVERE;
        return AQICategory.HAZARDOUS;
    }

    public AQICategory classifyO3(double concentration) {
        if (concentration <= 60) return AQICategory.GOOD;
        if (concentration <= 100) return AQICategory.SATISFACTORY;
        if (concentration <= 140) return AQICategory.MODERATE;
        if (concentration <= 180) return AQICategory.POOR;
        if (concentration <= 240) return AQICategory.SEVERE;
        return AQICategory.HAZARDOUS;
    }

    public AQICategory classifySO2(double concentration) {
        if (concentration <= 40) return AQICategory.GOOD;
        if (concentration <= 80) return AQICategory.SATISFACTORY;
        if (concentration <= 380) return AQICategory.MODERATE;
        if (concentration <= 800) return AQICategory.POOR;
        if (concentration <= 1600) return AQICategory.SEVERE;
        return AQICategory.HAZARDOUS;
    }

}
