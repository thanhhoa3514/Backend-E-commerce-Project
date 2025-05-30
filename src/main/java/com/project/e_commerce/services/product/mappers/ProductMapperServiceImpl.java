package com.project.e_commerce.services.product.mappers;

import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.models.product.Product;
import com.project.e_commerce.repositories.CategoryRepository;
import com.project.e_commerce.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMapperServiceImpl implements  IProductMapperService {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Product mapToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    @Override
    public ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(BigDecimal.valueOf(product.getPrice()))
            .thumbnail(product.getThumbnail())
            .description(product.getDescription())
            .categoryId(product.getCategoryId().getId())
            .quantity(product.getQuantity())
            .build();
    
        response.setCreated_at(product.getCreatedAt());
        response.setUpdated_at(product.getUpdatedAt());
        return response;
    }

    @Override
    public void updateProductFromDTO(Product product, ProductDTO productDTO) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setThumbnail(productDTO.getThumbnail());
    }

    @Override
    public void partialUpdateProductFromDTO(Product product, ProductDTO productDTO) {
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() > 0 && productDTO.getPrice() < 1000000) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getQuantity() > 0) {
            product.setQuantity(productDTO.getQuantity());
        }
        if (productDTO.getThumbnail() != null) {
            product.setThumbnail(productDTO.getThumbnail());
        }
    }

    @Override
    public Page<ProductResponse> mapToProductResponsePage(Page<Product> products) {
        return new PageImpl<>(
                products.getContent().stream()
                        .map(this::mapToProductResponse)
                        .collect(Collectors.toList()),
                products.getPageable(),
                products.getTotalElements()
        );
    }
}
