package com.project.e_commerce.dtos.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private List<ReviewDTO> reviews;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("total_items")
    private Long totalItems;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("average_rating")
    private Double averageRating;

    @JsonProperty("review_count")
    private Long reviewCount;
}