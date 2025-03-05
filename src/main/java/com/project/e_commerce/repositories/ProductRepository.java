package com.project.e_commerce.repositories;

import com.project.e_commerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);

    @Override
    Page<Product> findAll(Pageable pageable);
}
