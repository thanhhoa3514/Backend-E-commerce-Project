package com.project.e_commerce.repositories;

import com.project.e_commerce.models.Category;
import com.project.e_commerce.models.product.Product;

import org.springframework.lang.NonNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);
    @Override
    @NonNull
    Page<Product> findAll(@NonNull Pageable pageable);
    List<Product> findByCategory(Category category);
//    @Query("SELECT p FROM Product p WHERE " +
//            "(:categoryId IS NULL OR p.categoryId.id = :categoryId) AND " +
//            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
//            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
//            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")

    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.categoryId = :categoryId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> searchProducts
            (@Param("categoryId") Long categoryId,
             @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findProductsByIds(@Param("productIds") List<Long> productIds);

//    @Query("SELECT p FROM Product p JOIN p.favorites f WHERE f.user.id = :userId")
//    List<Product> findFavoriteProductsByUserId(@Param("userId") Long userId);
}
