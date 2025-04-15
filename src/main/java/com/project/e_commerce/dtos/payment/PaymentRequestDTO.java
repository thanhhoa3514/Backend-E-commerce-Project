package com.project.e_commerce.dtos.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Long amount;

    @JsonProperty("payment_method_id")
    private String paymentMethodId;

    private String currency;
    
    @JsonProperty("order_id")
    private Long orderId;
    
    @JsonProperty("payment_method_type")
    private String paymentMethodType;
}