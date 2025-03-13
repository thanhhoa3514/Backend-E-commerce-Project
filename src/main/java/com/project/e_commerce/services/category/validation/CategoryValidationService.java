package com.project.e_commerce.services.category.validation;


import com.project.e_commerce.exceptions.InvalidParamException;
import org.springframework.stereotype.Service;

@Service
public class CategoryValidationService {
    public void validateName(String name) throws InvalidParamException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidParamException("Category name cannot be empty");
        }
    }
}
