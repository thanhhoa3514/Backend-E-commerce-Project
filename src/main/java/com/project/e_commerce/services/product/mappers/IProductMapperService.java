package com.project.e_commerce.services.product.mappers;

import com.project.e_commerce.dtos.ProductDTO;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.responses.ProductResponse;

public interface IProductMapperService {

    Product mapToProduct(ProductDTO productDTO);
    ProductResponse mapToProductResponse(Product product);
    void updateProductFromDTO(Product product, ProductDTO productDTO);
    void partialUpdateProductFromDTO(Product product, ProductDTO productDTO);
}
