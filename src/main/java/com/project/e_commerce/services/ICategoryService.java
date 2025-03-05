package com.project.e_commerce.services;

import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;



public interface ICategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(long categoryId);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId,CategoryDTO category);
    void deleteCategory(long categoryId);
}
