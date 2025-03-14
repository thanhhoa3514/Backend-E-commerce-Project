package com.project.e_commerce.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum OrderStatus {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    SHIPPING("SHIPPING"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
    
    // Sửa lại phương thức convertStringToEnum
    public static OrderStatus convertStringToEnum(String enumString) {
        if (enumString == null || enumString.trim().isEmpty()) {
            return OrderStatus.PENDING;
        }
    
        String normalizedInput = enumString.trim().toUpperCase();
        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.name().equals(normalizedInput) 
                        || status.getStatus().equals(normalizedInput))
                .findFirst()
                .orElse(OrderStatus.PENDING);
    }
    public static OrderStatus fromString(String text) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.status.equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching OrderStatus for: " + text);
    }

}
