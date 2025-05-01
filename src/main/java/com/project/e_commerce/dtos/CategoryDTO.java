package com.project.e_commerce.dtos;

import jakarta.validation.constraints.NotEmpty;

import lombok.*;

@Data

public class CategoryDTO {
    @NotEmpty(message = "Category Name Can not be empty")
    private String name;
    
    private String description;

//    public @NotEmpty(message = "Category Name Can not be empty") String getName() {
//        return name;
//    }
//
//    public void setName(@NotEmpty(message = "Category Name Can not be empty") String name) {
//        this.name = name;
//    }
}
