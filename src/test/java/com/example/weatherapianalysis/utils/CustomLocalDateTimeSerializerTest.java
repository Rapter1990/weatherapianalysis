package com.example.weatherapianalysis.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomLocalDateTimeSerializerTest {

    private CustomLocalDateTimeSerializer serializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @BeforeEach
    void setUp() {
        serializer = new CustomLocalDateTimeSerializer();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    void testSerialize_ValidDate() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2025, 2, 15, 10, 30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        serializer.serialize(dateTime, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeString(dateTime.format(formatter));
    }

    @Test
    void testSerialize_NullValue() throws IOException {
        assertThrows(NullPointerException.class, () ->
                serializer.serialize(null, jsonGenerator, serializerProvider)
        );
    }

}