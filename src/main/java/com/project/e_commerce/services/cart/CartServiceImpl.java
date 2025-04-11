package com.project.e_commerce.services.cart;

import com.project.e_commerce.dtos.cart.CartItemDTO;
import com.project.e_commerce.dtos.cart.CartResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;

import com.project.e_commerce.models.CartItem;
import com.project.e_commerce.services.cart.commands.ICartCommandService;
import com.project.e_commerce.services.cart.mappers.ICartMapperService;
import com.project.e_commerce.services.cart.queries.ICartQueryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final ICartCommandService cartCommandService;
    private final ICartQueryService cartQueryService;
    private final ICartMapperService cartMapperService;



    @Override
    @Transactional
    public CartResponseDTO getCartByUser(Long userId) throws DataNotFoundException {
        List<CartItemDTO> cartItems = cartQueryService.getCartItems(userId);
        return cartMapperService.toResponseDTO(cartItems, "Cart items retrieved successfully");
    }

    @Override
    @Transactional
    public CartResponseDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) throws DataNotFoundException {
        cartCommandService.addToCart(cartItemDTO, userId);
        List<CartItemDTO> cartItems = cartQueryService.getCartItems(userId);
        return cartMapperService.toResponseDTO(cartItems, "Item added to cart successfully");
    }

    @Override
    @Transactional
    public CartResponseDTO updateCartItem(Long cartItemId, CartItemDTO cartItemDTO, Long userId) throws DataNotFoundException {
        cartCommandService.updateCartItem(cartItemId, cartItemDTO, userId);
        List<CartItemDTO> cartItems = cartQueryService.getCartItems(userId);
        return cartMapperService.toResponseDTO(cartItems, "Cart item updated successfully");
    }

    @Override
    @Transactional
    public CartResponseDTO removeItemFromCart(Long cartItemId,Long userId) throws DataNotFoundException {
        cartCommandService.removeFromCart(cartItemId, userId);
        List<CartItemDTO> cartItems = cartQueryService.getCartItems(userId);
        return cartMapperService.toResponseDTO(cartItems, "Item removed from cart successfully");
    }

    @Override
    @Transactional
    public CartResponseDTO clearCart(Long userId) throws DataNotFoundException {
        cartCommandService.clearCart(userId);
        List<CartItemDTO> cartItems = cartQueryService.getCartItems(userId);
        return cartMapperService.toResponseDTO(cartItems, "Cart cleared successfully");
    }

    @Override
    public CartItem findCartItemById(Long cartItemId, Long userId) {
        return cartQueryService.findCartItemById(cartItemId, userId);
    }


}
