package com.project.e_commerce.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "order_date")
    private LocalDateTime orderDate;


    @Column(name = "shipping_method")
    private String shippingMethod;

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
}
