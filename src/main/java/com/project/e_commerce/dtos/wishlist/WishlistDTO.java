package com.project.e_commerce.dtos.wishlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private Long id;
    private String name;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private List<WishlistItemDTO> items;
}