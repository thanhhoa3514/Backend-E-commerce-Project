package com.project.e_commerce.services.cart.commands;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.models.CartItem;

public interface ICartCommandService {
    CartItem addToCart(CartItemDTO cartItemDTO, Long userId);
    void updateCartItem(Long cartItemId, CartItemDTO cartItemDTO, Long userId);
    void removeFromCart(Long cartItemId, Long userId);
    void clearCart(Long userId);
}
