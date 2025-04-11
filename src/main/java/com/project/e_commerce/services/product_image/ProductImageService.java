package com.project.e_commerce.services.product_image;

import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.services.product_image.commands.IProductImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {
    private final IProductImageCommandService productImageCommandService;

    @Override
    public List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles) {
        return productImageCommandService.uploadProductImages(productId, multipartFiles);
    }
}
