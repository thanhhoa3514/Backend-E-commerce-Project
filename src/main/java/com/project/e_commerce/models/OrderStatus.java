package com.project.e_commerce.models;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum OrderStatus {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    SHIPPED("SHIPPED"),
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
    public static Optional<OrderStatus> convertStringToEnum(String enumString) {
        if (enumString == null || enumString.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalizedInput = enumString.trim().toUpperCase();
        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.name().equals(normalizedInput) 
                        || status.getStatus().equals(normalizedInput))
                .findFirst();
    }
    public static OrderStatus fromString(String text) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.status.equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching OrderStatus for: " + text);
    }
    // public static Optional<OrderStatus> convertStringToEnum(String enumString) {
    //     if (enumString == null || enumString.trim().isEmpty()) {
    //         return Optional.empty();
    //     }

    //     for (OrderStatus status : OrderStatus.values()) {
    //         if (status.name().equalsIgnoreCase(enumString.trim())) {
    //             return Optional.of(status);
    //         }
    //     }

    //     return Optional.empty();
    // }
}
