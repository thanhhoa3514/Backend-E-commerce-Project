package com.project.e_commerce.dtos.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class ProductDTO {
    @NotBlank
    @Size(min = 3, max = 50,message = "Title must be between 3 and 200 characters")
    private String name;

    private String description;

    @Min(value = 0,message = "Price must be greater than or equal to 0")
    @Max(value = 10000000,message = "Price must be less than or equal to 10,000,000")
    private double price;


    private int quantity;
    private String thumbnail;


    @JsonProperty("category_id")
    private Long categoryId;

//    private List<MultipartFile> multipartFiles;

}
