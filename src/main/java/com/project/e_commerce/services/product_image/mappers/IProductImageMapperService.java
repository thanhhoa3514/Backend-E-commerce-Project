package com.project.e_commerce.services.product_image.mappers;

import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.responses.ProductImageResponse;

public interface IProductImageMapperService {
    ProductImage mapToProductImage(ProductImageDTO productImageDTO);
    ProductImageResponse mapToProductImageResponse(ProductImage productImage);
}
