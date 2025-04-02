package com.project.e_commerce.services.cart.mappers;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.models.CartItem;

public interface ICartMapperService {
    CartItemDTO toDTO(CartItem cartItem);
    CartItem toEntity(CartItemDTO cartItemDTO);
}
