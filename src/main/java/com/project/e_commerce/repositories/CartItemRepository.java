package com.project.e_commerce.repositories;

import com.project.e_commerce.models.Cart;
import com.project.e_commerce.models.CartItem;
import com.project.e_commerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductAndColorAndSize(
            Cart cart, Product product, String color, String size);
    void deleteAllByCart(Cart cart);
    CartItem findByUserIdAndProductId(Long userId, Long productId);
    Optional<CartItem> findByIdAndUserId(Long id, Long userId);
    void deleteByUserId(Long userId);
    List<CartItem> findAllByUserId(Long userId);
    CartItem findByCartIdAndProductId(Long idCart, Long idCartItem);
}
