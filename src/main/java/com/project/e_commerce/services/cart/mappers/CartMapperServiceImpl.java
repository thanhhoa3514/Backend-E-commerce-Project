package com.project.e_commerce.services.cart.mappers;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.dtos.CartResponseDTO;
import com.project.e_commerce.models.CartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CartMapperServiceImpl implements ICartMapperService {
    @Override
    public CartItemDTO toDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        return CartItemDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productImage(cartItem.getProduct().getThumbnail())
                .price(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }

    @Override
    public CartItem toEntity(CartItemDTO cartItemDTO) {
        if (cartItemDTO == null) {
            return null;
        }

        return CartItem.builder()
                .id(cartItemDTO.getId())
                .quantity(cartItemDTO.getQuantity())
                .build();
    }

    @Override
    public CartResponseDTO toResponseDTO(List<CartItemDTO> cartItems, String message) {
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        int totalItems = cartItems.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();

        return CartResponseDTO.builder()
                .cartItems(cartItems)
                .totalPrice(totalPrice)
                .totalItems(totalItems)
                .message(message)
                .build();
    }
}
