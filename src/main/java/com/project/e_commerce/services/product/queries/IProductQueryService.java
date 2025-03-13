package com.project.e_commerce.services.product.queries;

import com.project.e_commerce.models.Product;
import com.project.e_commerce.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductQueryService {

    Product getProductById(long idProduct);

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
}
