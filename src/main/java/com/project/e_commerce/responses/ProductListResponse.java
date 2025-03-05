package com.project.e_commerce.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductListResponse {

    private List<ProductResponse> productResponsesList;
    private int totalPages;
}
