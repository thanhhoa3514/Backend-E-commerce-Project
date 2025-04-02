package com.project.e_commerce.responses;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.e_commerce.dtos.CartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("total_items")
    private Integer totalItems;

    private String message;


}
