package com.project.e_commerce.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import com.project.e_commerce.enums.ShippingMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    @JsonIgnore
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

    private String notes;

    @JsonProperty("total_price")
    @Min(value = 1,message = "Total money must be greater than zero")
    private Double totalPrice;

    @NotNull(message = "Shipping method is required")
    private ShippingMethod shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonIgnore
    private LocalDateTime shippingDate;

    @JsonIgnore
    private LocalDateTime estimatedDeliveryFrom;

    @JsonIgnore
    private LocalDateTime estimatedDeliveryTo;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private List<OrderDetailDTO> orderDetails;
}
