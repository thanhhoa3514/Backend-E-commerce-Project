package com.project.e_commerce.controllers;


import com.project.e_commerce.dtos.cart.CartItemDTO;
import com.project.e_commerce.dtos.cart.CartResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.responses.ResponseObject;
import com.project.e_commerce.services.cart.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<ResponseObject> getCart(Authentication authentication) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        CartResponseDTO cart = cartService.getCartByUser(user.getId());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Get cart successfully")
                        .data(cart)
                        .build()
        );
    }


    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addItemToCart(
            Authentication authentication,
            @RequestBody @Valid CartItemDTO cartItemDTO) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        CartResponseDTO cart = cartService.addItemToCart(user.getId(), cartItemDTO);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Item added to cart successfully")
                        .data(cart)
                        .build()
        );
    }


    @PutMapping("/items/{itemId}")
    public ResponseEntity<ResponseObject> updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemDTO cartItemDTO) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        CartResponseDTO cart = cartService.updateCartItem(cartItemId, cartItemDTO, user.getId());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Cart item updated successfully")
                        .data(cart)
                        .build()
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ResponseObject> removeItemFromCart(
            Authentication authentication,
            @PathVariable Long cartItemId) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();
        CartResponseDTO cart = cartService.removeItemFromCart(cartItemId, user.getId());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Item removed from cart successfully")
                        .data(cart)
                        .build()
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseObject> clearCart(Authentication authentication) throws DataNotFoundException {
        User user = (User) authentication.getPrincipal();

        // Clear cart
        CartResponseDTO cart = cartService.clearCart(user.getId());

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Cart cleared successfully")
                        .data(cart)
                        .build()
        );
    }
}
