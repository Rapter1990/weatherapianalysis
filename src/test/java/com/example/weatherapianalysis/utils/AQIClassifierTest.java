package com.example.weatherapianalysis.utils;

import com.example.weatherapianalysis.model.enums.AQICategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AQIClassifierTest {

    @Test
    void testClassifyCO() {
        assertEquals(AQICategory.GOOD, AQIClassifier.classifyCO(4000));
        assertEquals(AQICategory.SATISFACTORY, AQIClassifier.classifyCO(9000));
        assertEquals(AQICategory.MODERATE, AQIClassifier.classifyCO(12000));
        assertEquals(AQICategory.POOR, AQIClassifier.classifyCO(15000));
        assertEquals(AQICategory.SEVERE, AQIClassifier.classifyCO(30000));
        assertEquals(AQICategory.HAZARDOUS, AQIClassifier.classifyCO(35000));
    }

    @Test
    void testClassifyO3() {
        assertEquals(AQICategory.GOOD, AQIClassifier.classifyO3(50));
        assertEquals(AQICategory.SATISFACTORY, AQIClassifier.classifyO3(90));
        assertEquals(AQICategory.MODERATE, AQIClassifier.classifyO3(130));
        assertEquals(AQICategory.POOR, AQIClassifier.classifyO3(170));
        assertEquals(AQICategory.SEVERE, AQIClassifier.classifyO3(230));
        assertEquals(AQICategory.HAZARDOUS, AQIClassifier.classifyO3(250));
    }

    @Test
    void testClassifySO2() {
        assertEquals(AQICategory.GOOD, AQIClassifier.classifySO2(30));
        assertEquals(AQICategory.SATISFACTORY, AQIClassifier.classifySO2(70));
        assertEquals(AQICategory.MODERATE, AQIClassifier.classifySO2(350));
        assertEquals(AQICategory.POOR, AQIClassifier.classifySO2(750));
        assertEquals(AQICategory.SEVERE, AQIClassifier.classifySO2(1500));
        assertEquals(AQICategory.HAZARDOUS, AQIClassifier.classifySO2(1700));
    }

}