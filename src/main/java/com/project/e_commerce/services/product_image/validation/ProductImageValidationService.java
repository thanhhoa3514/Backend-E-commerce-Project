package com.project.e_commerce.services.product_image.validation;


import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.utils.FileValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageValidationService {
    private final FileValidationUtil fileValidationUtil;

    public void validateImageCount(List<MultipartFile> multipartFiles) {
        if (multipartFiles.size() > ProductImage.MAXIMUM_IMAGES_PER_ONE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "More than " + ProductImage.MAXIMUM_IMAGES_PER_ONE + " images found"
            );
        }
    }

    public void validateSingleFile(MultipartFile file) {
        if (file.isEmpty()) {
            return;
        }

        if (!fileValidationUtil.isImageFile(file)) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "File must be an image. Detected type: " + fileValidationUtil.getFileType(file)
            );
        }

        if (!fileValidationUtil.isValidImageSize(file, 10 * 1024 * 1024)) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "File is too big. Maximum size is 10MB"
            );
        }
    }
}
