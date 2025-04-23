package com.project.e_commerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.e_commerce.models.Product;
import lombok.*;

import java.math.BigDecimal;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse{

    private Long id;
    private String name;

    private String description;


    private BigDecimal price;


    private int quantity;
    private String thumbnail;
    private int totalPages;


    @JsonProperty("category_id")
    private Long categoryId;


    public static ProductResponse from(Product product) {
        ProductResponse productResponse = ProductResponse
                .builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(BigDecimal.valueOf(product.getPrice()))
                .quantity(product.getQuantity())
                .thumbnail(product.getThumbnail())
                .categoryId(product.getCategoryId().getId())
                .build();
            productResponse.setCreated_at(product.getCreatedAt());
            productResponse.setUpdated_at(product.getUpdatedAt());
        return productResponse;

    }

}
