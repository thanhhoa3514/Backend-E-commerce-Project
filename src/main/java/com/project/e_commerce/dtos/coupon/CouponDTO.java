package com.project.e_commerce.dtos.coupon;

import com.project.e_commerce.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private Long id;
    
    @NotBlank(message = "Coupon code is required")
    private String code;
    
    private String description;
    
    @NotNull(message = "Active status is required")
    private boolean active;
    
    private LocalDateTime validFrom;
    
    private LocalDateTime validTo;
    
    private Integer usageLimit;
    
    private BigDecimal minimumOrderAmount;
    
    @NotNull(message = "Discount type is required")
    private DiscountType discountType;
    
    private List<CouponConditionDTO> conditions;
} 