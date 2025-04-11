package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.wishlist.WishlistDTO;
import com.project.e_commerce.dtos.wishlist.WishlistRequestDTO;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.services.wishlist.IWishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/wishlists")
@RequiredArgsConstructor
@Tag(name = "Wishlists", description = "Wishlist management APIs")
public class WishlistController {
    private final IWishListService wishlistService;

    @GetMapping
    @Operation(summary = "Get user wishlists", description = "Retrieves all wishlists for the authenticated user")
    public ResponseEntity<List<WishlistDTO>> getUserWishlists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((com.project.e_commerce.models.user.User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(wishlistService.getUserWishlists(userId));
    }

    @PostMapping
    @Operation(summary = "Create wishlist", description = "Creates a new wishlist for the authenticated user")
    public ResponseEntity<WishlistDTO> createWishlist(@RequestBody WishlistRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((com.project.e_commerce.models.user.User) authentication.getPrincipal()).getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishlistService.createWishlist(userId, request.getName()));
    }

    @PostMapping("/{wishlistId}/products/{productId}")
    @Operation(summary = "Add product to wishlist", description = "Adds a product to a wishlist")
    public ResponseEntity<WishlistDTO> addProductToWishlist(
            @PathVariable Long wishlistId,
            @PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((com.project.e_commerce.models.user.User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(wishlistService.addProductToWishlist(wishlistId, productId, userId));
    }

    @DeleteMapping("/{wishlistId}/products/{productId}")
    @Operation(summary = "Remove product from wishlist", description = "Removes a product from a wishlist")
    public ResponseEntity<WishlistDTO> removeProductFromWishlist(
            @PathVariable Long wishlistId,
            @PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((com.project.e_commerce.models.user.User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(wishlistService.removeProductFromWishlist(wishlistId, productId, userId));
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long wishlistId) {
        User currentUser = getCurrentUser();
        wishlistService.deleteWishlist(wishlistId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}