package com.project.e_commerce.services.product_image.mappers;

import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.models.ProductImage;

public class ProductImageMapperServiceImpl implements  IProductImageMapperService {
    @Override
    public ProductImage mapToProductImage(ProductImageDTO productImageDTO) {
        return ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .build();
    }

    @Override
    public ProductImageResponse mapToProductImageResponse(ProductImage productImage) {
        return ProductImageResponse.builder()
                .id(productImage.getId())
                .productId(productImage.getProduct().getId())
                .imageUrl(productImage.getImageUrl())
                .build();
    }
}
