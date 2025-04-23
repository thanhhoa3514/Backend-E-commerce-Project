package com.project.e_commerce.mappers;

import com.project.e_commerce.models.product.Product;
import com.project.e_commerce.responses.ProductResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {
    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(BigDecimal.valueOf(product.getPrice()))
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategoryId() != null ? product.getCategoryId().getId() : null)
                .build();
    }
}
