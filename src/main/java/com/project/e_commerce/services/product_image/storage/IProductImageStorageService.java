package com.project.e_commerce.services.product_image.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IProductImageStorageService {
    String storeFile(MultipartFile file) throws IOException;
}
