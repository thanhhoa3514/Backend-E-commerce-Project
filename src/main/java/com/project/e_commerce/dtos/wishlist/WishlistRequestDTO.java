package com.project.e_commerce.dtos.wishlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequestDTO {

    @NotBlank(message = "Wishlist name cannot be blank")
    @Size(min = 1, max = 100, message = "Wishlist name must be between 1 and 100 characters")
    private String name;
}