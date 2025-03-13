package com.project.e_commerce.services.product.commands;

import com.project.e_commerce.dtos.ProductDTO;
import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductCommandService {
    public Product createProduct(ProductDTO productDTO);
    Product getProductById(long idProduct);

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long idProduct, ProductDTO product);
    void deleteProduct(long idProduct);
    boolean exitsByName(String nameProduct);
    ProductImage createImagesForProduct(
            Long idProduct,
            ProductImageDTO productImageDTO) throws InvalidParamException;
}
