package com.project.e_commerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product extends BaseEntity {
    @Column(name = "name", nullable = false,length = 350)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;


    @Column(name = "thumbnail")
    private String thumbnail;


    @Column(name = "quantity",nullable = false)
    @Min(0)
    private int quantity;

    @Column(name="description")
    private String description;



    @NotNull(message = "Category ID must not be null")
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category categoryId;

}
