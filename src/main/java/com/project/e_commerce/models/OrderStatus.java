package com.project.e_commerce.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
    public static OrderStatus fromString(String text) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching OrderStatus for: " + text);
    }
}
