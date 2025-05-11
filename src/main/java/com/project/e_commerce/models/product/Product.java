package com.project.e_commerce.models.product;

import com.project.e_commerce.models.BaseEntity;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.models.ProductImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
// Event-driven approach with Spring Data JPA
// @EntityListeners(ProductListener.class)
public class Product extends BaseEntity {
    @Column(name = "name", nullable = false, length = 350)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "quantity", nullable = false)
    @Min(0)
    private int quantity;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Category ID must not be null")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductImage> productImages;
}
