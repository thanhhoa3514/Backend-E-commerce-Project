package com.project.e_commerce.repositories;

import com.project.e_commerce.models.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    @Query("SELECT o FROM Order o WHERE o.active = true AND (:keyword IS NULL OR :keyword = '' OR " +
            "o.fullname LIKE %:keyword% " +
            "OR o.address LIKE %:keyword% " +
            "OR o.notes LIKE %:keyword% " +
            "OR o.email LIKE %:keyword%)")
    Page<Order> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
