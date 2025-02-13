package com.example.weatherapianalysis.model.enums;

public enum AQICategory {
    GOOD("Good"),
    SATISFACTORY("Satisfactory"),
    MODERATE("Moderate"),
    POOR("Poor"),
    SEVERE("Severe"),
    HAZARDOUS("Hazardous");

    private final String label;

    AQICategory(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
