package com.project.e_commerce.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private Integer quantity;
    private String color;
    private String size;
    private BigDecimal itemTotal;
    private String createdAt;
    private String updatedAt;
}
