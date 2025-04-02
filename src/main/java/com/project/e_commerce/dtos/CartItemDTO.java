package com.project.e_commerce.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private Long id;

    @JsonProperty("product_id")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_image")
    private String productImage;

    private Double price;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @JsonProperty("total_price")
    private Double totalPrice;
}
