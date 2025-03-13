package com.project.e_commerce.services.category.commands;

import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Category;

public interface ICategoryCommandService {
    Category createCategory(CategoryDTO categoryDTO) throws InvalidParamException;
    Category updateCategory(long categoryId, CategoryDTO categoryDTO) throws InvalidParamException;
    void deleteCategory(long categoryId);
}
