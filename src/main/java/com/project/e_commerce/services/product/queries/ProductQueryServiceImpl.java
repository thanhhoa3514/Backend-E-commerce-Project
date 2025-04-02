package com.project.e_commerce.services.product.queries;

import com.project.e_commerce.dtos.ProductDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.ProductResponse;
import com.project.e_commerce.services.product.mappers.IProductMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueryServiceImpl implements IProductQueryService {


    private final ProductRepository productRepository;
    private final IProductMapperService productMapperService;


    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllWithPagination(String keyword, Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return false;
    }
}
