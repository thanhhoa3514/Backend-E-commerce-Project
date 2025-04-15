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
public class PaymentResponseDTO {
    @JsonProperty("payment_intent_id")
    private String paymentIntentId;

    // This is only returned to the client and never stored in database
    // It's used once by the frontend to complete the payment flow
    @JsonProperty("client_secret")
    private String clientSecret;

    private String status;

    private String message;
}