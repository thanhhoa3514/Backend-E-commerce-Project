package com.project.e_commerce.services.product_image.commands;

import com.project.e_commerce.dtos.product.ProductImageDTO;

import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.repositories.ProductImageRepository;
import com.project.e_commerce.services.product.ProductService;
import com.project.e_commerce.services.product.valiadation.ProductValidationService;
import com.project.e_commerce.services.product_image.storage.IProductImageStorageService;

import com.project.e_commerce.services.product_image.validation.ProductImageValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageCommandServiceImpl implements IProductImageCommandService{

    @Lazy
    private final ProductService productService;
    private final ProductValidationService productValidationService;
    private final IProductImageStorageService storageService;
    private final ProductImageValidationService productImageValidationService;
    private static final String UPLOADS_FOLDER = "uploads";

    @Override
    public List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles) {
        Product existingProduct = productService.getProductById(productId);
        multipartFiles = multipartFiles == null ? new ArrayList<>() : multipartFiles;

        productValidationService.validateProductImages(multipartFiles);
        ArrayList<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            productValidationService.validateProductImage(file);

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

    @Override
    @Transactional
    public List<ProductImage> updateAllProductImages(Product product, List<MultipartFile> multipartFiles) {

        Product existingProduct = productService.getProductById(product.getId());
        multipartFiles = multipartFiles == null ? new ArrayList<>() : multipartFiles;

        ArrayList<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            productImageValidationService.validateSingleFile(file);

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

    @Override
    public void deleteFile(String filename) throws IOException {
        // Đường dẫn đến thư mục chứa file
        java.nio.file.Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Đường dẫn đầy đủ đến file cần xóa
        java.nio.file.Path filePath = uploadDir.resolve(filename);

        // Kiểm tra xem file tồn tại hay không
        if (Files.exists(filePath)) {
            // Xóa file
            Files.delete(filePath);
        } else {
            throw new FileNotFoundException("File not found: " + filename);
        }
    }
}
