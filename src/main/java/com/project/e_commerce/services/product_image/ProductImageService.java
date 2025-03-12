package com.project.e_commerce.services.product_image;

import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.services.product.ProductService;
import com.project.e_commerce.utils.FileValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageService implements IProductImageService {
    private final ProductService productService;
    private final FileValidationUtil fileValidationUtil;
    @Autowired
    public ProductImageService(
            ProductService productService,
            FileValidationUtil fileValidationUtil
    ) {
        this.productService = productService;
        this.fileValidationUtil = fileValidationUtil;
    }
    private void validateImageCount(List<MultipartFile> multipartFiles) {
        if (multipartFiles.size() > ProductImage.MAXIMUM_IMAGES_PER_ONE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "More than " + ProductImage.MAXIMUM_IMAGES_PER_ONE + " images found"
            );
        }
    }

    private void validateSingleFile(MultipartFile file) {
        // Skip empty files
        if (file.isEmpty()) {
            return;
        }

        // Validate file type
        if (!fileValidationUtil.isImageFile(file)) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "File must be an image. Detected type: " + fileValidationUtil.getFileType(file)
            );
        }

        // Validate file size (10MB limit)
        if (!fileValidationUtil.isValidImageSize(file, 10 * 1024 * 1024)) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "File is too big. Maximum size is 10MB"
            );
        }
    }
    private String storeSingleFile(MultipartFile file) throws IOException {
        // Clean filename
        String fileName = org.springframework.util.StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "unnamed"
        );

        // Create unique filename
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        // Create upload directory if not exists
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Destination path
        Path destinationPath = Paths.get(uploadDir.toString(), uniqueFileName);

        // Copy file
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }
    private ProductImage createProductImage(Long productId, String fileName) throws InvalidParamException {
        return productService.createImagesForProduct(
                productId,
                ProductImageDTO.builder()
                        .imageUrl(fileName)
                        .build()
        );
    }

    @Override
    public List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles) {
        // Validate product exists
        Product existingProduct = productService.getProductById(productId);

        // Null check and initialize empty list if needed
        multipartFiles = multipartFiles == null ? new ArrayList<>() : multipartFiles;

        // Check maximum number of images
        validateImageCount(multipartFiles);


        List<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            // Skip empty files
            validateSingleFile(file);


            // Store file and create product image
            try {
                String fileName = storeSingleFile(file);
                ProductImage productImage = createProductImage(existingProduct.getId(), fileName);
                productImages.add(productImage);

            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to upload file: " + e.getMessage()
                );
            } catch (InvalidParamException e) {
                throw new RuntimeException(e);
            }
        }

        return productImages;
    }
//    private String storeFile(MultipartFile file) throws IOException {
//        // Clean filename
//        String fileName = org.springframework.util.StringUtils.cleanPath(
//                file.getOriginalFilename() != null ? file.getOriginalFilename() : "unnamed"
//        );
//
//        // Create unique filename
//        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
//
//        // Create upload directory if not exists
//        Path uploadDir = Paths.get("uploads");
//        if (!Files.exists(uploadDir)) {
//            Files.createDirectories(uploadDir);
//        }
//
//        // Destination path
//        Path destinationPath = Paths.get(uploadDir.toString(), uniqueFileName);
//
//        // Copy file
//        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
//
//        return uniqueFileName;
//    }
}
