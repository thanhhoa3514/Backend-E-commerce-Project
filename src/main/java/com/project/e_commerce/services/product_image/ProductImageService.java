package com.project.e_commerce.services.product_image;

import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.services.product.ProductService;
import com.project.e_commerce.services.product_image.commands.IProductImageCommandService;
import com.project.e_commerce.utils.FileValidationUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {
    private final IProductImageCommandService productImageCommandService;

    @Override
    public List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> multipartFiles) {
        return productImageCommandService.uploadProductImages(productId, multipartFiles);
    }
}
