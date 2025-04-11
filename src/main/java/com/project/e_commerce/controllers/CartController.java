package com.project.e_commerce.controllers;


import com.project.e_commerce.dtos.cart.CartItemDTO;
import com.project.e_commerce.dtos.cart.CartResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.user.User;
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
        return ResponseEntity.ok(cartService.getCartByUser(user.getId()));
    }


    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            Authentication authentication,
            @RequestBody @Valid CartItemDTO cartItemDTO) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.addItemToCart(user.getId(), cartItemDTO));
    }


    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,

            @RequestBody @Valid CartItemDTO cartItemDTO) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.updateCartItem(cartItemId, cartItemDTO, user.getId()));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(
            Authentication authentication,
            @PathVariable Long cartItemId) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.removeItemFromCart(cartItemId, user.getId()));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartResponseDTO> clearCart(Authentication authentication) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();

        // Clear cart
        CartResponseDTO response = cartService.clearCart(user.getId());

        return ResponseEntity.ok(response);
    }
}
