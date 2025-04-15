package com.project.e_commerce.models;


import com.project.e_commerce.models.enums.OrderStatus;
import com.project.e_commerce.enums.ShippingMethod;
import com.project.e_commerce.models.converters.ShippingMethodConverter;
import com.project.e_commerce.models.coupons.Coupon;
import com.project.e_commerce.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "fullname", nullable = false)
    private String fullname;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "notes")
    private String notes;

    @Column(name = "estimated_delivery_from")
    private LocalDateTime estimatedDeliveryFrom;

    @Column(name = "estimated_delivery_to")
    private LocalDateTime estimatedDeliveryTo;

    @Column(name = "order_date")
    private LocalDateTime orderDate;


    @Column(name = "shipping_method")
    @Convert(converter = ShippingMethodConverter.class)
    private ShippingMethod shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "active")
    private boolean active;

    @Column(name = "total_price")
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "discount_amount", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "final_amount", precision = 10, scale = 2)
    private BigDecimal finalAmount;
}
