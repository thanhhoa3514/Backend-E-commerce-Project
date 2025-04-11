package com.project.e_commerce.services.cart;

import com.project.e_commerce.dtos.cart.CartItemDTO;
import com.project.e_commerce.dtos.cart.CartResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.CartItem;

public interface ICartService {
    CartResponseDTO getCartByUser(Long userId) throws DataNotFoundException;

    CartResponseDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) throws DataNotFoundException;

    CartResponseDTO updateCartItem(Long cartItemId, CartItemDTO cartItemDTO,  Long userId) throws DataNotFoundException;

    CartResponseDTO removeItemFromCart(Long cartItemId,Long userId) throws DataNotFoundException;

    CartResponseDTO clearCart(Long userId) throws DataNotFoundException;
    CartItem findCartItemById(Long cartItemId, Long userId);
}
