package com.project.e_commerce.models;

import com.project.e_commerce.responses.ProductResponse;
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
    private ProductResponse product;
    private Integer quantity;
    private String color;
    private String size;
    private BigDecimal itemTotal;
    private String createdAt;
    private String updatedAt;
}
