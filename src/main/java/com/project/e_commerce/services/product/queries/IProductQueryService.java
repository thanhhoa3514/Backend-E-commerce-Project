package com.project.e_commerce.services.product.queries;

import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductQueryService {

    Product getProductById(long productId);
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    boolean existsByName(String name);
    List<Product> findAll();
    Page<ProductDTO> findAllWithPagination(String keyword, Long categoryId, Pageable pageable);

    boolean existsById(Long id);
}
