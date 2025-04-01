package com.project.e_commerce.services.cart;

import com.project.e_commerce.dtos.CartItemDTO;
import com.project.e_commerce.dtos.CartResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.*;
import com.project.e_commerce.repositories.CartItemRepository;
import com.project.e_commerce.repositories.CartRepository;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.responses.ProductResponse;
import com.project.e_commerce.services.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;



    @Override
    @Transactional
    public CartResponseDTO getCartByUser(User user) throws DataNotFoundException {
        Cart cart = getOrCreateCart(user);
        return mapToCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addItemToCart(User user, CartItemDTO cartItemDTO) throws DataNotFoundException {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProductAndColorAndSize(
                cart, product, cartItemDTO.getColor(), cartItemDTO.getSize());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(item);
        }else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemDTO.getQuantity())
                    .color(cartItemDTO.getColor())
                    .size(cartItemDTO.getSize())
                    .build();
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        return mapToCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateCartItem(User user, Long itemId, CartItemDTO cartItemDTO) throws DataNotFoundException {
        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new DataNotFoundException("Item does not belong to user's cart");
        }

        item.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(item);

        return mapToCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeItemFromCart(User user, Long itemId) throws DataNotFoundException {
        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new DataNotFoundException("Item does not belong to user's cart");
        }

        cart.removeItem(item);
        cartItemRepository.delete(item);

        return mapToCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public void clearCart(User user) throws DataNotFoundException {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteAllByCart(cart);
        cart.getCartItems().clear();
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private CartResponseDTO mapToCartResponseDTO(Cart cart) {
        List<CartItemResponseDTO> items = cart.getCartItems().stream()
                .map(this::mapToCartItemResponseDTO)
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(CartItemResponseDTO::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponseDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalPrice(totalPrice)
                .createdAt(cart.getCreatedAt().toString())
                .updatedAt(cart.getUpdatedAt().toString())
                .build();
    }

    private CartItemResponseDTO mapToCartItemResponseDTO(CartItem item) {
        ProductResponse product = productMapper.toProductResponseDTO(item.getProduct());
        BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemResponseDTO.builder()
                .id(item.getId())
                .product(product)
                .quantity(item.getQuantity())
                .color(item.getColor())
                .size(item.getSize())
                .itemTotal(itemTotal)
                .createdAt(item.getCreatedAt().toString())
                .updatedAt(item.getUpdatedAt().toString())
                .build();
    }
}
