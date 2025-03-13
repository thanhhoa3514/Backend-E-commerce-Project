package com.project.e_commerce.services.product.commands;

import com.project.e_commerce.dtos.ProductDTO;
import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;


public interface IProductCommandService {
    Product createProduct(ProductDTO productDTO)throws InvalidParamException;
    Product updateProduct(long productId, ProductDTO productDTO);
    void deleteProduct(long productId);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)throws InvalidParamException;
}
