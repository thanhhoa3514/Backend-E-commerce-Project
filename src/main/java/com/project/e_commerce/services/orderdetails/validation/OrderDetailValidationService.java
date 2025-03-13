package com.project.e_commerce.services.orderdetails.validation;

import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.OrderDetail;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailValidationService {
    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new DataNotFoundException("Quantity must be greater than 0");
        }
    }

    public void validateTotalMoney(double totalMoney) {
        if (totalMoney <= 0) {
            throw new DataNotFoundException("Total money must be greater than 0");
        }
    }

    public void validateForDeletion(OrderDetail orderDetail) {
        if (orderDetail.getOrder().getOrderStatus().name().equals("COMPLETED")) {
            throw new DataNotFoundException("Cannot delete detail of completed order");
        }
    }
}
