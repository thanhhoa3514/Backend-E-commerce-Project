package com.project.e_commerce.services.cart.mappers;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.dtos.CartResponseDTO;
import com.project.e_commerce.models.CartItem;
import com.project.e_commerce.responses.CartResponse;

import java.util.List;

public interface ICartMapperService {
    CartItemDTO toDTO(CartItem cartItem);
    CartItem toEntity(CartItemDTO cartItemDTO);
    CartResponseDTO toResponseDTO(List<CartItemDTO> cartItems, String message);
}
