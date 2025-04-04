package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.wishlist.WishlistDTO;
import com.project.e_commerce.models.User;
import com.project.e_commerce.services.wishlist.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/wishlists")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<WishlistDTO>> getUserWishlists() {
        User currentUser = getCurrentUser();
        List<WishlistDTO> wishlists = wishlistService.getUserWishlists(currentUser.getId());
        return ResponseEntity.ok(wishlists);
    }

    @PostMapping
    public ResponseEntity<WishlistDTO> createWishlist(@RequestBody Map<String, String> request) {
        User currentUser = getCurrentUser();
        String name = request.get("name");
        WishlistDTO wishlist = wishlistService.createWishlist(currentUser.getId(), name);
        return new ResponseEntity<>(wishlist, HttpStatus.CREATED);
    }

    @PostMapping("/{wishlistId}/products/{productId}")
    public ResponseEntity<WishlistDTO> addProductToWishlist(
            @PathVariable Long wishlistId,
            @PathVariable Long productId) {
        User currentUser = getCurrentUser();
        WishlistDTO wishlist = wishlistService.addProductToWishlist(wishlistId, productId, currentUser.getId());
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/{wishlistId}/products/{productId}")
    public ResponseEntity<WishlistDTO> removeProductFromWishlist(
            @PathVariable Long wishlistId,
            @PathVariable Long productId) {
        User currentUser = getCurrentUser();
        WishlistDTO wishlist = wishlistService.removeProductFromWishlist(wishlistId, productId, currentUser.getId());
        return ResponseEntity.ok(wishlist);
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