package com.project.e_commerce.enums;

import lombok.Getter;

@Getter
public enum ShippingMethod {
    EXPRESS(1, 2, "Giao hàng nhanh"),
    STANDARD(3, 5, "Giao hàng tiêu chuẩn"),
    SAVING(5, 7, "Giao hàng tiết kiệm");

    private final int minDays;
    private final int maxDays;
    private final String description;

    ShippingMethod(int minDays, int maxDays, String description) {
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.description = description;
    }
} 