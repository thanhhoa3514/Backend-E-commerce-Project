package com.project.e_commerce.dtos.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageDTO {

    @JsonProperty(value="product_id")
    @Min(value = 1,message = "Product's id must be greater than 0")
    private Long productId;


    @Size(min=5, max=200,message = "Name must be between 5 and 200 characters")
    @JsonProperty(value="image_url")
    private String imageUrl;
}
