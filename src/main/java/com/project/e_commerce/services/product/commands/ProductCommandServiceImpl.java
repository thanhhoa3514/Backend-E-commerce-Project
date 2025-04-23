package com.project.e_commerce.services.product.commands;

import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.dtos.product.ProductImageDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.models.product.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.repositories.CategoryRepository;
import com.project.e_commerce.repositories.ProductImageRepository;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.services.product.mappers.IProductMapperService;
import com.project.e_commerce.services.product.valiadation.ProductValidationService;
import com.project.e_commerce.services.product_image.commands.IProductImageCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandServiceImpl implements IProductCommandService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final IProductMapperService productMapperService;
    private final ProductValidationService productValidationService;

    private final IProductImageCommandService productImageCommandService;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws InvalidParamException{
        productValidationService.validateName(productDTO.getName());
        productValidationService.validatePrice(productDTO.getPrice());
        productValidationService.validateQuantity(productDTO.getQuantity());
        productValidationService.validateProductDTO(productDTO);


        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id: " + productDTO.getCategoryId()));

        Product product = productMapperService.mapToProduct(productDTO);
        product.setCategoryId(category);

        log.info("Created product with ID: {}", product.getId());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + productId));

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find category with id: " + productDTO.getCategoryId()));
            existingProduct.setCategoryId(category);
        }

        productMapperService.partialUpdateProductFromDTO(existingProduct, productDTO);
        log.info("Updated product with ID: {}", existingProduct.getId());
        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + productId));
        productRepository.delete(product);
        log.info("Deleted product with ID: {}", productId);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
            throws InvalidParamException
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + productId));

        int imageCount = productImageRepository.findByProductId(productId).size();
        if (imageCount >= ProductImage.MAXIMUM_IMAGES_PER_ONE) {
            throw new InvalidParamException("Each product can only have up to 5 images.");
        }

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        return productImageRepository.save(productImage);
    }
}
