package com.project.e_commerce.models.coupons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "coupon_conditions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    @JsonBackReference
    private Coupon coupon;

    @Column(name = "attribute", nullable = false)
    private String attribute;

    @Column(name = "operator", nullable = false)
    private String operator;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;
}
