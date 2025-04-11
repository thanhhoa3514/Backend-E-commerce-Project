package com.project.e_commerce.services.cart.queries;

import com.project.e_commerce.dtos.cart.CartItemDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.CartItem;
import com.project.e_commerce.repositories.CartItemRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.cart.mappers.ICartMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartQueryServiceImpl implements ICartQueryService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ICartMapperService cartMapperService;


    @Override
    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItems(Long userId) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User not found with id: " + userId);
        }

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findAllByUserId(userId);

        // Map to DTOs
        return cartItems.stream()
                .map(cartMapperService::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CartItem findCartItemById(Long cartItemId, Long userId) {
        return cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found with id: " + cartItemId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long cartItemId) {
        return cartItemRepository.existsById(cartItemId);
    }
}
