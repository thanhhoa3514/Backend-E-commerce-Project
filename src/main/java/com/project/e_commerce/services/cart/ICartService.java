package com.project.e_commerce.services.cart;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.dtos.CartResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.User;

public interface ICartService {
    CartResponseDTO getCartByUser(User user) throws DataNotFoundException;
    CartResponseDTO addItemToCart(User user, CartItemDTO cartItemDTO) throws DataNotFoundException;
    CartResponseDTO updateCartItem(User user, Long itemId, CartItemDTO cartItemDTO) throws DataNotFoundException;
    CartResponseDTO removeItemFromCart(User user, Long itemId) throws DataNotFoundException;
    void clearCart(User user) throws DataNotFoundException;
}
