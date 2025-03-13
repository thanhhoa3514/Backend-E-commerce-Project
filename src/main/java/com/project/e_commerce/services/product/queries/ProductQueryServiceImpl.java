package com.project.e_commerce.services.product.queries;

import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.ProductResponse;
import com.project.e_commerce.services.product.mappers.IProductMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements IProductQueryService {


    private final ProductRepository productRepository;
    private final IProductMapperService productMapperService;


    @Override
    public Product getProductById(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        Page<Product> productsPage = productRepository.findAll(pageRequest);
        return productsPage.map(productMapperService::mapToProductResponse);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
}
