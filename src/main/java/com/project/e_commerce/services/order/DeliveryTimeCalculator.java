package com.project.e_commerce.services.order;

import com.project.e_commerce.enums.ShippingMethod;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryTimeCalculator {
    
    public DeliveryTimeRange calculateDeliveryTime(ShippingMethod shippingMethod) {
        LocalDateTime now = LocalDateTime.now();
        
        LocalDateTime fromDate = now.plusDays(shippingMethod.getMinDays());
        LocalDateTime toDate = now.plusDays(shippingMethod.getMaxDays());
        
        return new DeliveryTimeRange(fromDate, toDate);
    }

    @Getter
    public static class DeliveryTimeRange {
        private final LocalDateTime fromDate;
        private final LocalDateTime toDate;

        public DeliveryTimeRange(LocalDateTime fromDate, LocalDateTime toDate) {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
    }
} 