package com.project.e_commerce.services.product_image.commands;

import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.services.product.ProductService;
import com.project.e_commerce.services.product_image.storage.IProductImageStorageService;
import com.project.e_commerce.services.product_image.validation.ProductImageValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductImageCommandServiceImpl implements IProductImageCommandService{


    private final ProductService productService;
    private final ProductImageValidationService validationService;
    private final IProductImageStorageService storageService;
    @Override
    public List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles) {
        Product existingProduct = productService.getProductById(productId);
        multipartFiles = multipartFiles == null ? new ArrayList<>() : multipartFiles;

        validationService.validateImageCount(multipartFiles);
        List<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            validationService.validateSingleFile(file);

            try {
                String fileName = storageService.storeFile(file);
                ProductImage productImage = productService.createImagesForProduct(
                        existingProduct.getId(),
                        ProductImageDTO.builder().imageUrl(fileName).build()
                );
                productImages.add(productImage);
            } catch (IOException | InvalidParamException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to upload file: " + e.getMessage()
                );
            }
        }

        return productImages;
    }
}
