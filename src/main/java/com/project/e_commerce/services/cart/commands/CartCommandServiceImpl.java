package com.project.e_commerce.services.cart.commands;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidDataException;
import com.project.e_commerce.models.CartItem;
//import jakarta.transaction.Transactional;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.CartItemRepository;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartCommandServiceImpl implements ICartCommandService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;






    @Override
    @Transactional
    public CartItem addToCart(CartItemDTO cartItemDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with id: " + userId));

        // Validate product
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + cartItemDTO.getProductId()));

        // Validate quantity
        if (cartItemDTO.getQuantity() <= 0) {
            throw new InvalidDataException("Quantity must be greater than 0");
        }
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartItemDTO.getProductId());

        if (existingCartItem != null) {
            // Update quantity if product already in cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            log.info("Updated cart item quantity for user: {}, product: {}", userId, cartItemDTO.getProductId());
            return cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem newCartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(cartItemDTO.getQuantity())
                    .build();

            log.info("Added new item to cart for user: {}, product: {}", userId, cartItemDTO.getProductId());
            return cartItemRepository.save(newCartItem);
        }
    }

    @Override
    @Transactional
    public void updateCartItem(Long cartItemId, CartItemDTO cartItemDTO, Long userId) {
        // Find cart item
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found with id: " + cartItemId));

        // Validate quantity
        if (cartItemDTO.getQuantity() <= 0) {
            throw new InvalidDataException("Quantity must be greater than 0");
        }

        // Update quantity
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItem);
        log.info("Updated cart item: {} for user: {}", cartItemId, userId);
    }

    @Override
    @Transactional
    public void removeFromCart(Long cartItemId, Long userId) {
        // Find cart item
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found with id: " + cartItemId));

        // Remove from cart
        cartItemRepository.delete(cartItem);
        log.info("Removed item from cart: {} for user: {}", cartItemId, userId);
    }

    @Override
    public void clearCart(Long userId) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User not found with id: " + userId);
        }

        // Clear all cart items for user
        cartItemRepository.deleteByUserId(userId);
        log.info("Cleared cart for user: {}", userId);
    }
}
