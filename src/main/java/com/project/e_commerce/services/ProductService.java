package com.project.e_commerce.services;

import com.project.e_commerce.dtos.ProductDTO;
import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.GlobalExceptionHandler;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.repositories.CategoryRepository;
import com.project.e_commerce.repositories.ProductImageRepository;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.ProductResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {

        if (productDTO.getCategoryId() != null){
            Category existingCategory=categoryRepository.findById(productDTO
                            .getCategoryId())
                    .orElseThrow(() ->new
                            DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));

            Product product = Product
                    .builder()
                    .name(productDTO.getName())
                    .description(productDTO.getDescription())
                    .price(productDTO.getPrice())
                    .quantity(productDTO.getQuantity())
                    .thumbnail(productDTO.getThumbnail())
                    .categoryId(existingCategory)
                    .build();

            return productRepository.save(product);
        }else {
            throw new IllegalArgumentException("Category ID must be provided");
        }

    }

    @Override
    public Product getProductById(long idProduct) {

        return productRepository
                .findById(idProduct)
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id: " + idProduct));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {

        return productRepository
                .findAll(pageRequest)
                .map(ProductResponse::from);
    }

    @Override
    public Product updateProduct(long idProduct, ProductDTO productDTO) {

        Product existingProduct=getProductById(idProduct);

        if (existingProduct == null) {
            throw new DataNotFoundException("Cannot find product with id: " + idProduct);
        }
        if (productDTO.getCategoryId() != null) {
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException("Cannot find category with id: " +
                                    productDTO.getCategoryId()));
            existingProduct.setCategoryId(existingCategory);
        }

            if (productDTO.getName() != null) {
                existingProduct.setName(productDTO.getName());
            }
            if (productDTO.getDescription() != null) {
                existingProduct.setDescription(productDTO.getDescription());
            }
            if (productDTO.getPrice() > 0 && productDTO.getPrice() < 1000000) {
                existingProduct.setPrice(productDTO.getPrice());
            }
            if (productDTO.getQuantity()>0) {
                existingProduct.setQuantity(productDTO.getQuantity());
            }
            if (productDTO.getThumbnail() != null) {
                existingProduct.setThumbnail(productDTO.getThumbnail());
            }

            return productRepository.save(existingProduct);

    }

    @Override
    public void deleteProduct(long idProduct) {

        Optional<Product> existingProduct=productRepository.findById(idProduct);

        if(existingProduct.isPresent()) {

            productRepository.delete(existingProduct.get());

        }else {

            throw new DataNotFoundException("Cannot find product with id: " + idProduct);
        }

    }

    @Override
    public boolean exitsByName(String nameProduct) {

        return productRepository.existsByName(nameProduct);

    }

    @Override
    public ProductImage createImagesForProduct(
            Long idProduct,
            ProductImageDTO productImageDTO) throws InvalidParamException {

        Product existingProduct=productRepository
                .findById(idProduct)
                .orElseThrow(()->new DataNotFoundException(
                        "Cannot find product with id: " + productImageDTO.getProductId()));
        ProductImage productImage= ProductImage
                .builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        int numberOfImagesInProduct=productImageRepository.findByProductId(existingProduct.getId()).size();
        if(numberOfImagesInProduct>=ProductImage.MAXIMUM_IMAGES_PER_ONE) {
            throw new InvalidParamException("Each product can only have up to 5 images.");
        }
        return productImageRepository.save(productImage);
    }
}
