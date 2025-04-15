package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.coupon.ApplyCouponRequest;
import com.project.e_commerce.dtos.coupon.CouponDTO;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.coupons.Coupon;
import com.project.e_commerce.services.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        Coupon coupon = couponService.createCoupon(couponDTO);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Coupon> getCouponByCode(@PathVariable String code) {
        Coupon coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CouponDTO couponDTO) {
        Coupon updatedCoupon = couponService.updateCoupon(id, couponDTO);
        return ResponseEntity.ok(updatedCoupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/apply")
    public ResponseEntity<Order> applyCoupon(@Valid @RequestBody ApplyCouponRequest request) {
        Order order = couponService.applyCouponToOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateCoupon(
            @RequestParam String code,
            @RequestParam Long userId,
            @RequestParam Double orderAmount) {
        boolean isValid = couponService.isValid(code, userId, java.math.BigDecimal.valueOf(orderAmount));
        return ResponseEntity.ok(isValid);
    }
} 