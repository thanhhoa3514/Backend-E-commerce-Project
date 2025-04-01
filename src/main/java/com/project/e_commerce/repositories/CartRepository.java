package com.project.e_commerce.repositories;

import com.project.e_commerce.models.Cart;
import com.project.e_commerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    boolean existsByUser(User user);
}
