package com.project.e_commerce.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.e_commerce.models.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse extends BaseResponse{

    private Long id;


    @JsonProperty("user_id")
    private Long userId;


    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;


    @JsonProperty("address")
    private String address;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;


    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;


    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("total_price")
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private OrderStatus orderStatus;

}
