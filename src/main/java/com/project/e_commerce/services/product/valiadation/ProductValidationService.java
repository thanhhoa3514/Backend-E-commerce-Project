package com.project.e_commerce.services.product.valiadation;


import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.exceptions.InvalidDataException;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import com.project.e_commerce.models.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductValidationService {

    private final CategoryRepository categoryRepository;

    /**
     * Validates a product DTO for creation or update operations
     * @param productDTO The product data to validate
     */
    public void validateProductDTO(ProductDTO productDTO) {
        // Category existence validation
        if (productDTO.getCategoryId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(productDTO.getCategoryId());
            if (categoryOptional.isEmpty()) {
                throw new InvalidDataException("Category with ID " + productDTO.getCategoryId() + " does not exist");
            }
        }
        // Price validation (additional business rules beyond annotations)

        // Additional business rule validations can be added here
        log.debug("Product DTO validation passed for product: {}", productDTO.getName());
    }

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

    /**
     * Validates product images for upload
     * @param files The image files to validate
     */
    public void validateProductImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new InvalidDataException("No image files provided");
        }

        // Check maximum number of images
        if (files.size() > 10) {
            throw new InvalidDataException("Maximum 10 images allowed per product");
        }

        // Validate each file
        for (MultipartFile file : files) {
            validateProductImage(file);
        }

        log.debug("Product images validation passed for {} images", files.size());
    }

    public void validateProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidDataException("Empty file provided");
        }

        // Check file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new InvalidDataException("File size exceeds maximum limit of 5MB");
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidDataException("Only image files are allowed");
        }

        // Additional image validations can be added here
        log.debug("Product image validation passed for file: {}", file.getOriginalFilename());
    }
}
