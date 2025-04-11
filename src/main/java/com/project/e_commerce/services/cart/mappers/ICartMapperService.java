package com.project.e_commerce.services.cart.mappers;

import com.project.e_commerce.dtos.cart.CartItemDTO;
import com.project.e_commerce.dtos.cart.CartResponseDTO;
import com.project.e_commerce.models.CartItem;

import java.util.List;

public interface ICartMapperService {
    CartItemDTO toDTO(CartItem cartItem);
    CartItem toEntity(CartItemDTO cartItemDTO);
    CartResponseDTO toResponseDTO(List<CartItemDTO> cartItems, String message);
}
