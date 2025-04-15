package com.project.e_commerce.dtos.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponConditionDTO {
    private Long id;
    
    @NotBlank(message = "Attribute is required")
    private String attribute;
    
    @NotBlank(message = "Operator is required")
    private String operator;
    
    @NotBlank(message = "Value is required")
    private String value;
    
    @NotNull(message = "Discount amount is required")
    private BigDecimal discountAmount;
} 