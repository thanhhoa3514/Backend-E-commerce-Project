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
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @JsonIgnore
    @JsonProperty("user_id")
    @Min(value = 1,message = "User's id must be greater than 0")
    private Long userId;

    @JsonProperty("fullname")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
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

    @JsonProperty("payment_method")
    private String paymentMethod;

    private List<OrderDetailDTO> orderDetails;
}
