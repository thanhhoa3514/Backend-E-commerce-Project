package com.project.e_commerce.models.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class NotificationPreferencesConverter implements AttributeConverter<NotificationPreferences, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(NotificationPreferences attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Error converting notification preferences to JSON", e);
            return null;
        }
    }

    @Override
    public NotificationPreferences convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(dbData, NotificationPreferences.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to notification preferences", e);
            return null;
        }
    }
} 