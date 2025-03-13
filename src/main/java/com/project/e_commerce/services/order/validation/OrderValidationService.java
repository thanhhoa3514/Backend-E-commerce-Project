package com.project.e_commerce.services.order.validation;


import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderValidationService {

    public void validateShippingDate(LocalDateTime shippingDate) {
        if (shippingDate != null && shippingDate.isBefore(LocalDateTime.now())) {
            throw new DataNotFoundException("Shipping Date must be after the current date");
        }
    }

    public void validateOrderForDeletion(Order order) {
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new DataNotFoundException("Cannot delete completed order");
        }
    }
}
