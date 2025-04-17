package com.project.e_commerce.services.coupon;

import com.project.e_commerce.dtos.coupon.ApplyCouponRequest;
import com.project.e_commerce.dtos.coupon.CouponDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.coupons.Coupon;
import com.project.e_commerce.models.coupons.CouponCondition;
import com.project.e_commerce.enums.DiscountType;
import com.project.e_commerce.repositories.CouponRepository;
import com.project.e_commerce.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Coupon createCoupon(CouponDTO couponDTO) {
        // Implementation for creating a new coupon
        Coupon coupon = Coupon.builder()
                .code(couponDTO.getCode())
                .description(couponDTO.getDescription())
                .active(couponDTO.isActive())
                .validFrom(couponDTO.getValidFrom())
                .validTo(couponDTO.getValidTo())
                .usageLimit(couponDTO.getUsageLimit())
                .usageCount(0)
                .minimumOrderAmount(couponDTO.getMinimumOrderAmount())
                .discountType(couponDTO.getDiscountType())
                .build();
        
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Coupon not found with id: " + id));
    }

    @Override
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new DataNotFoundException("Coupon not found with code: " + code));
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @Transactional
    public Coupon updateCoupon(Long id, CouponDTO couponDTO) {
        Coupon existingCoupon = getCouponById(id);
        
        existingCoupon.setCode(couponDTO.getCode());
        existingCoupon.setDescription(couponDTO.getDescription());
        existingCoupon.setActive(couponDTO.isActive());
        existingCoupon.setValidFrom(couponDTO.getValidFrom());
        existingCoupon.setValidTo(couponDTO.getValidTo());
        existingCoupon.setUsageLimit(couponDTO.getUsageLimit());
        existingCoupon.setMinimumOrderAmount(couponDTO.getMinimumOrderAmount());
        existingCoupon.setDiscountType(couponDTO.getDiscountType());
        
        return couponRepository.save(existingCoupon);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = getCouponById(id);
        couponRepository.delete(coupon);
    }

    @Override
    public boolean isValid(String couponCode, Long userId, BigDecimal orderAmount) {
        try {
            Coupon coupon = getCouponByCode(couponCode);
            
            // Check if coupon is active
            if (!coupon.isActive()) {
                return false;
            }
            
            // Check validity period
            LocalDateTime now = LocalDateTime.now();
            if (coupon.getValidFrom() != null && now.isBefore(coupon.getValidFrom())) {
                return false;
            }
            if (coupon.getValidTo() != null && now.isAfter(coupon.getValidTo())) {
                return false;
            }
            
            // Check usage limit
            if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
                return false;
            }
            
            // Check minimum order amount
            if (coupon.getMinimumOrderAmount() != null && 
                orderAmount.compareTo(coupon.getMinimumOrderAmount()) < 0) {
                return false;
            }
            
            // Check conditions
            if (!coupon.getConditions().isEmpty()) {
                boolean allConditionsMet = evaluateConditions(coupon, orderAmount);
                if (!allConditionsMet) {
                    return false;
                }
            }
            
            return true;
        } catch (DataNotFoundException e) {
            return false;
        }
    }

    private boolean evaluateConditions(Coupon coupon, BigDecimal orderAmount) {
        // Simplified condition evaluation logic
        for (CouponCondition condition : coupon.getConditions()) {
            if ("minimum_amount".equals(condition.getAttribute())) {
                BigDecimal minAmount = new BigDecimal(condition.getValue());
                if (">".equals(condition.getOperator()) && 
                    orderAmount.compareTo(minAmount) <= 0) {
                    return false;
                }
            }
            // Add more condition evaluations as needed
        }
        return true;
    }

    @Override
    public BigDecimal calculateDiscount(String couponCode, Order order) {
        Coupon coupon = getCouponByCode(couponCode);
        BigDecimal orderTotal = order.getFinalAmount();
        BigDecimal discount = BigDecimal.ZERO;
        
        // Apply main coupon discount
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            // Assume discount value is stored in the first condition's discountAmount
            BigDecimal discountPercent = coupon.getConditions().get(0).getDiscountAmount();
            discount = orderTotal.multiply(discountPercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            BigDecimal discountAmount = coupon.getConditions().get(0).getDiscountAmount();
            discount = discountAmount.min(orderTotal); // Don't discount more than order total
        }
        
        // Apply additional condition discounts (if any)
        for (CouponCondition condition : coupon.getConditions()) {
            boolean conditionMet = evaluateSingleCondition(condition, order);
            if (conditionMet) {
                BigDecimal additionalDiscount = condition.getDiscountAmount();
                if ("percentage".equals(condition.getOperator())) {
                    additionalDiscount = orderTotal.multiply(
                        additionalDiscount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                }
                discount = discount.add(additionalDiscount);
            }
        }
        
        // Don't allow discount to exceed order total
        return discount.min(orderTotal);
    }
    
    private boolean evaluateSingleCondition(CouponCondition condition, Order order) {
        // Simplified evaluation of a single condition
        if ("minimum_amount".equals(condition.getAttribute())) {
            BigDecimal minAmount = new BigDecimal(condition.getValue());
            if (">".equals(condition.getOperator())) {
                return order.getFinalAmount().compareTo(minAmount) > 0;
            }
        }
        // Add more condition evaluations as needed
        return false;
    }

    @Override
    @Transactional
    public Order applyCouponToOrder(ApplyCouponRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + request.getOrderId()));
        
        String couponCode = request.getCouponCode();
        
        // Check if coupon is valid
        if (!isValid(couponCode, order.getUser().getId(), order.getFinalAmount())) {
            try {
                throw new InvalidParamException("Coupon is not valid for this order");
            } catch (InvalidParamException e) {
                throw new RuntimeException(e);
            }
        }
        
        // Calculate discount
        BigDecimal discount = calculateDiscount(couponCode, order);
        
        // Apply discount to order
        order.setDiscountAmount(discount);
        order.setFinalAmount(order.getFinalAmount().subtract(discount));
        
        // Increment coupon usage count
        Coupon coupon = getCouponByCode(couponCode);
        coupon.setUsageCount(coupon.getUsageCount() + 1);
        couponRepository.save(coupon);
        
        return orderRepository.save(order);
    }
} 