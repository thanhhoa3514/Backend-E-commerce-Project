package com.project.e_commerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;



@Builder
public class ProductResponse extends BaseResponse{

    private String name;

    private String description;


    private double price;


    private int quantity;
    private String thumbnail;


    @JsonProperty("category_id")
    private Long categoryId;


}
