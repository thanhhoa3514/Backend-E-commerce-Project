package com.project.e_commerce.services.product_image.commands;

import com.project.e_commerce.dtos.product.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.product.Product;
import com.project.e_commerce.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    void deleteFile(String filename) throws IOException;
    
    /**
     * Create a new ProductImage for a product
     * @param product The product to create image for (already loaded from DB)
     * @param imageUrl The URL of the image
     * @return The created ProductImage entity
     * @throws InvalidParamException if validation fails
     */
    ProductImage createProductImage(Product product, String imageUrl) throws InvalidParamException;
}
