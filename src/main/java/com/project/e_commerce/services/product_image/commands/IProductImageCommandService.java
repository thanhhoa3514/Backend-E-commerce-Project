package com.project.e_commerce.services.product_image.commands;

import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageCommandService {
    List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles);


    /**
     * Updates all images for a product by removing existing ones and uploading new ones
     * @param product The product to update images for
     * @param files The new image files to upload
     * @return List of created product images
     */
    List<ProductImage> updateAllProductImages(Product product, List<MultipartFile> files);
}
