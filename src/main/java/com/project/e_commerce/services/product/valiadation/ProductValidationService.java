package com.project.e_commerce.services.product.valiadation;


import com.project.e_commerce.exceptions.InvalidParamException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductValidationService {
    public void validatePrice(double price) throws InvalidParamException{
        if (price <= 0 || price >= 1000000) {
            throw new InvalidParamException("Price must be between 0 and 1,000,000");
        }
    }

    public void validateQuantity(int quantity)throws InvalidParamException {
        if (quantity < 0) {
            throw new InvalidParamException("Quantity cannot be negative");
        }
    }

    public void validateName(String name) throws InvalidParamException{
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidParamException("Product name cannot be empty");
        }
    }
}
