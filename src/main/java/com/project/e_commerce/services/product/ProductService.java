package com.project.e_commerce.services.product;

import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.dtos.product.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.ProductResponse;
import com.project.e_commerce.services.product.commands.IProductCommandService;
import com.project.e_commerce.services.product.mappers.IProductMapperService;
import com.project.e_commerce.services.product.queries.IProductQueryService;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {


    private final IProductCommandService productCommandService;
    private final IProductQueryService productQueryService;
    private final ProductRepository productRepository;
    private final IProductMapperService productMapperService;

    @Override
    public Product createProduct(ProductDTO productDTO) throws InvalidParamException {
        return productCommandService.createProduct(productDTO);
    }

    @Override
    @Cacheable(value = "products", key = "#productId")
    public Product getProductById(long productId) {

        return productQueryService.getProductById(productId);
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {

        return productQueryService.getAllProducts(pageRequest);
    }

    @Override
    @CacheEvict(value = {"products", "productSearch"}, key = "#productId")
    public Product updateProduct(long productId, ProductDTO productDTO) {
        return productCommandService.updateProduct(productId, productDTO);
    }

    @Override
    @CacheEvict(value = {"products", "productSearch"}, key = "#productId")
    public void deleteProduct(long productId) {
        productCommandService.deleteProduct(productId);
    }

    @Override
    public boolean exitsByName(String nameProduct) {

        return productQueryService.existsByName(nameProduct);

    }

    @Override
    public ProductImage createImagesForProduct(
            Long productId,
            ProductImageDTO productImageDTO) throws InvalidParamException {

        return productCommandService.createProductImage(productId, productImageDTO);
    }

    @Cacheable(value = "productSearch",
            key = "#categoryId + '_' + #minPrice + '_' + #maxPrice + '_' + #keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<ProductDTO> searchProducts(
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            String keyword,
            Pageable pageable) {

        Page<Product> products = productRepository.searchProducts(
                categoryId, minPrice, maxPrice, keyword, pageable);

        return productMapperService.mapToProductResponse(products);
    }
}
