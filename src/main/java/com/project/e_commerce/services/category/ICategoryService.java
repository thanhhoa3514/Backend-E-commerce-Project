package com.project.e_commerce.services.category;

import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.responses.CategoryResponse;

import java.util.List;



public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO)throws InvalidParamException;
    Category getCategoryById(long categoryId);
    List<CategoryResponse> getAllCategories();
    Category updateCategory(long categoryId,CategoryDTO category)  throws InvalidParamException;
    void deleteCategory(long categoryId);
}
