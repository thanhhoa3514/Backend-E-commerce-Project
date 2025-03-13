package com.project.e_commerce.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse{
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;  // Chỉ trả về tên người dùng

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

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("status")
    private OrderStatus orderStatus;

    private String estimatedDeliveryTime;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getFullName())  // Chỉ lấy tên user
                .fullName(order.getFullname())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .notes(order.getNotes())
                .orderDate(order.getOrderDate())
                .shippingMethod(order.getShippingMethod())
                .shippingAddress(order.getShippingAddress())
                .trackingNumber(order.getTrackingNumber())
                .shippingDate(order.getShippingDate())
                .paymentMethod(order.getPaymentMethod())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

}
