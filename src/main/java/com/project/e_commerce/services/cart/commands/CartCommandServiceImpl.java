package com.project.e_commerce.services.cart.commands;

import com.project.e_commerce.dtos.cart.CartItemDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidDataException;
import com.project.e_commerce.models.Cart;
import com.project.e_commerce.models.CartItem;
//import jakarta.transaction.Transactional;
import com.project.e_commerce.models.product.Product;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.CartItemRepository;
import com.project.e_commerce.repositories.CartRepository;
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
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



    @Override
    @Transactional
    public CartItem addToCart(CartItemDTO cartItemDTO, Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new DataNotFoundException("User not found"));
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });

        // Validate product
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        // Validate quantity
        if (cartItemDTO.getQuantity() <= 0) {
            throw new InvalidDataException("Quantity must be greater than 0");
        }

        // Check if product already in cart
        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), cartItemDTO.getProductId());

        if (existingCartItem != null) {
            // Update quantity if product already in cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            log.info("Updated cart item quantity for user: {}, product: {}", userId, cartItemDTO.getProductId());
            return cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
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
        CartItem cartItem = cartItemRepository.findByIdAndCartUserId(cartItemId, userId)
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
        CartItem cartItem = cartItemRepository.findByIdAndCartUserId(cartItemId, userId)
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
        cartItemRepository.deleteByCartUserId(userId);
        log.info("Cleared cart for user: {}", userId);
    }
}
