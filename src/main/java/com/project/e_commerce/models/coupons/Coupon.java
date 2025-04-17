package com.project.e_commerce.models.coupons;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.e_commerce.enums.DiscountType;
import com.project.e_commerce.models.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "active", nullable = false)
    private boolean active;
    
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    
    @Column(name = "valid_to")
    private LocalDateTime validTo;
    
    @Column(name = "usage_limit")
    private Integer usageLimit;
    
    @Column(name = "usage_count", columnDefinition = "integer default 0")
    private Integer usageCount = 0;
    
    @Column(name = "minimum_order_amount")
    private BigDecimal minimumOrderAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;
    
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CouponCondition> conditions = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "coupon_categories",
        joinColumns = @JoinColumn(name = "coupon_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> applicableCategories = new ArrayList<>();
    
    // Helper method to add a condition
    public void addCondition(CouponCondition condition) {
        conditions.add(condition);
        condition.setCoupon(this);
    }
    
    // Helper method to remove a condition
    public void removeCondition(CouponCondition condition) {
        conditions.remove(condition);
        condition.setCoupon(null);
    }
}
