package com.project.e_commerce.dtos.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCouponRequest {
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotBlank(message = "Coupon code is required")
    private String couponCode;
} 