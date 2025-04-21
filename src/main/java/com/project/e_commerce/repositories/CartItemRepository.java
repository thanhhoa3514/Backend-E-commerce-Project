package com.project.e_commerce.repositories;

import com.project.e_commerce.models.Cart;
import com.project.e_commerce.models.CartItem;
import com.project.e_commerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductAndColorAndSize(
            Cart cart, Product product, String color, String size);
    void deleteAllByCart(Cart cart);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.user.id = :userId AND ci.product.id = :productId")
    CartItem findByCartUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.id = :id AND ci.cart.user.id = :userId")
    Optional<CartItem> findByIdAndCartUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.user.id = :userId")
    void deleteByCartUserId(@Param("userId") Long userId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.user.id = :userId")
    List<CartItem> findAllByCartUserId(@Param("userId") Long userId);
    
    CartItem findByCartIdAndProductId(Long cartId, Long productId);
}
