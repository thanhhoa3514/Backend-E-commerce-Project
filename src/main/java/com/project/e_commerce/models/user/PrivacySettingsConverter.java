package com.project.e_commerce.models.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class PrivacySettingsConverter implements AttributeConverter<PrivacySettings, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PrivacySettings attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Error converting privacy settings to JSON", e);
            return null;
        }
    }

    @Override
    public PrivacySettings convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(dbData, PrivacySettings.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to privacy settings", e);
            return null;
        }
    }
}