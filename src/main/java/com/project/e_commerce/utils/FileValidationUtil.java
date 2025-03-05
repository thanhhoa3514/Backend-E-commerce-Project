package com.project.e_commerce.utils;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileValidationUtil {
    private static final Tika tika = new Tika();


    public boolean isImageFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String mimeType = tika.detect(inputStream);
            return mimeType != null && mimeType.startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }
    public String getFileType(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return tika.detect(inputStream);
        } catch (IOException e) {
            return null;
        }
    }
    public boolean isValidImageSize(MultipartFile file, long maxSizeInBytes) {
        return file.getSize() <= maxSizeInBytes;
    }
}
