package com.project.e_commerce.dtos.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("total_items")
    private Integer totalItems;

    private String message;
}
