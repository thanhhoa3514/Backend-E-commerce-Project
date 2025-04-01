package com.project.e_commerce.controllers;


import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.dtos.CartResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.User;
import com.project.e_commerce.services.cart.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(Authentication authentication) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.getCartByUser(user));
    }


    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            Authentication authentication,
            @RequestBody @Valid CartItemDTO cartItemDTO) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.addItemToCart(user, cartItemDTO));
    }


    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateCartItem(
            Authentication authentication,
            @PathVariable Long itemId,
            @RequestBody @Valid CartItemDTO cartItemDTO) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.updateCartItem(user, itemId, cartItemDTO));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(
            Authentication authentication,
            @PathVariable Long itemId) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.removeItemFromCart(user, itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}
