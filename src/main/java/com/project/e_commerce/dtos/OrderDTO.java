package com.project.e_commerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1,message = "User's id must be greater than 0")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;


    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number must be at least 5 characters")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("total_price")
    @Min(value = 1,message = "Total money must be greater than zero")
    private Double totalPrice;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;


    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;


    @JsonProperty("payment_method")
    private String paymentMethod;


}
