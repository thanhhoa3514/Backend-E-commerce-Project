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
public class PaymentResponse {
    @JsonProperty("payment_intent_id")
    private String paymentIntentId;

    @JsonProperty("client_secret")
    private String clientSecret;

    private String status;

    private String message;
}