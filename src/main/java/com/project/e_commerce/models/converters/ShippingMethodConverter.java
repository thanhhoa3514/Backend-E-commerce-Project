package com.project.e_commerce.models.converters;

import com.project.e_commerce.enums.ShippingMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ShippingMethodConverter implements AttributeConverter<ShippingMethod, String> {

    @Override
    public String convertToDatabaseColumn(ShippingMethod shippingMethod) {
        if (shippingMethod == null) {
            return null;
        }
        return shippingMethod.name();
    }

    @Override
    public ShippingMethod convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        // Xử lý các giá trị legacy
        if (dbData.equals("van chuyen")) {
            return ShippingMethod.STANDARD;
        }
        
        try {
            return ShippingMethod.valueOf(dbData);
        } catch (IllegalArgumentException e) {

            System.err.println("Unknown shipping method: " + dbData);
            return ShippingMethod.STANDARD;
        }
    }
} 