package com.project.e_commerce.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("product_name")
    private String productName;

    private int quantity;

    @JsonProperty("total_money")
    private double totalMoney;

    private String color;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
