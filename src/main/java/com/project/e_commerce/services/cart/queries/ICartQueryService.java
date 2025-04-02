package com.project.e_commerce.services.cart.queries;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.models.CartItem;

import java.util.List;

public interface ICartQueryService {
    List<CartItemDTO> getCartItems(Long userId);
    CartItem findCartItemById(Long cartItemId, Long userId);
    boolean existsById(Long cartItemId);
}
