package com.project.e_commerce.models.enums;

public enum ShippingMethod {
    EXPRESS(1, 2),      // 1-2 ngày
    STANDARD(3, 5),     // 3-5 ngày
    SAVING(5, 7);       // 5-7 ngày

    private final int minDays;
    private final int maxDays;

    ShippingMethod(int minDays, int maxDays) {
        this.minDays = minDays;
        this.maxDays = maxDays;
    }

    public int getMinDays() {
        return minDays;
    }

    public int getMaxDays() {
        return maxDays;
    }
}
