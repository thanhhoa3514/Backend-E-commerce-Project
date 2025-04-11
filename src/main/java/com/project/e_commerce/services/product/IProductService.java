package com.project.e_commerce.services.product;


import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.dtos.product.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;



public interface IProductService{
     Product createProduct(ProductDTO productDTO) throws InvalidParamException;
    Product getProductById(long productId);

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long productId, ProductDTO product);
    void deleteProduct(long productId);

    boolean exitsByName(String nameProduct);
    ProductImage createImagesForProduct(
            Long productId,
            ProductImageDTO productImageDTO) throws InvalidParamException;
}
