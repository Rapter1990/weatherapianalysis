package com.example.weatherapianalysis.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OpenWeatherResponse {
    private Coord coord;
    private List<DataPoint> list;

    @Getter
    @Setter
    public static class Coord {
        private double lat;
        private double lon;
    }

    @Getter
    @Setter
    public static class DataPoint {
        private long dt;
        private Main main;
        private Components components;
    }

    @Getter
    @Setter
    public static class Main {
        private int aqi;
    }

    @Getter
    @Setter
    public static class Components {
        private double co;
        private double no;
        private double no2;
        private double o3;
        private double so2;
        private double pm2_5;
        private double pm10;
        private double nh3;
    }
}
