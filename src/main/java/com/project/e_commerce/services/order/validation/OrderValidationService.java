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

    public void validateStatusTransition(Order order, OrderStatus newStatus) {
        OrderStatus currentStatus = order.getOrderStatus();
        
        // Không thể chuyển từ COMPLETED sang status khác
        if (currentStatus == OrderStatus.COMPLETED) {
            throw new DataNotFoundException("Cannot change status of completed order");
        }
        
        // Không thể chuyển từ CANCELLED sang status khác
        if (currentStatus == OrderStatus.CANCELLED) {
            throw new DataNotFoundException("Cannot change status of cancelled order");
        }
        
        // Kiểm tra logic chuyển trạng thái
        switch (newStatus) {
            case PROCESSING:
                if (currentStatus != OrderStatus.PENDING) {
                    throw new DataNotFoundException("Order must be PENDING to move to PROCESSING");
                }
                break;
            case SHIPPING:
                if (currentStatus != OrderStatus.PROCESSING) {
                    throw new DataNotFoundException("Order must be PROCESSING to move to SHIPPING");
                }
                break;
            case COMPLETED:
                if (currentStatus != OrderStatus.SHIPPING) {
                    throw new DataNotFoundException("Order must be SHIPPING to move to COMPLETED");
                }
                break;
            case CANCELLED:
                if (currentStatus == OrderStatus.COMPLETED) {
                    throw new DataNotFoundException("Cannot cancel completed order");
                }
                break;
        }
    }
}
