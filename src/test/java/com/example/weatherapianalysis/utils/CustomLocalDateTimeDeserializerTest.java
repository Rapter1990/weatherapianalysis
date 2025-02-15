package com.example.weatherapianalysis.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomLocalDateTimeDeserializerTest {

    private CustomLocalDateTimeDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @BeforeEach
    void setUp() {
        deserializer = new CustomLocalDateTimeDeserializer();
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
    }

    @Test
    void testDeserialize_ValidDate() throws IOException {
        when(jsonParser.getText()).thenReturn("15-02-2025");
        LocalDateTime result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 2, 15).atStartOfDay(), result);
    }

    @Test
    void testDeserialize_NullValue() throws IOException {
        when(jsonParser.getText()).thenReturn(null);
        LocalDateTime result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    void testDeserialize_EmptyValue() throws IOException {
        when(jsonParser.getText()).thenReturn("");
        LocalDateTime result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    void testDeserialize_InvalidDate() throws IOException {
        when(jsonParser.getText()).thenReturn("invalid-date");
        assertThrows(DateTimeParseException.class, () ->
                deserializer.deserialize(jsonParser, deserializationContext)
        );
    }

    @Test
    void testDeserialize_InvalidFormat() throws IOException {
        when(jsonParser.getText()).thenReturn("2025/02/15");
        assertThrows(DateTimeParseException.class, () ->
                deserializer.deserialize(jsonParser, deserializationContext)
        );
    }

}