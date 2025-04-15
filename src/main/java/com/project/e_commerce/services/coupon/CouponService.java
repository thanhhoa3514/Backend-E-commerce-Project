package com.project.e_commerce.services.coupon;

import com.project.e_commerce.dtos.coupon.ApplyCouponRequest;
import com.project.e_commerce.dtos.coupon.CouponDTO;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.coupons.Coupon;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {
    // CRUD operations
    Coupon createCoupon(CouponDTO couponDTO);
    Coupon getCouponById(Long id);
    Coupon getCouponByCode(String code);
    List<Coupon> getAllCoupons();
    Coupon updateCoupon(Long id, CouponDTO couponDTO);
    void deleteCoupon(Long id);
    
    // Validation and calculation
    boolean isValid(String couponCode, Long userId, BigDecimal orderAmount);
    BigDecimal calculateDiscount(String couponCode, Order order);
    
    // Application
    Order applyCouponToOrder(ApplyCouponRequest request);
} 