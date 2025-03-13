package com.project.e_commerce.services.product_image.commands;

import com.project.e_commerce.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageCommandService {
    List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles);
}
