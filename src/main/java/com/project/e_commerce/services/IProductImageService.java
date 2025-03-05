package com.project.e_commerce.services;

import com.project.e_commerce.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageService {
    /**
     * Upload images for a specific product
     *
     * @param productId The ID of the product to upload images for
     * @param multipartFiles List of files to upload
     * @return List of created ProductImage entities
     */
    List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles);
}
