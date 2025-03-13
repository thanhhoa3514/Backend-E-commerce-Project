package com.project.e_commerce.services.category.mappers;

import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.responses.CategoryResponse;

public interface ICategoryMapperService {
    Category mapToCategory(CategoryDTO categoryDTO);
    CategoryResponse mapToCategoryResponse(Category category);
    void updateCategoryFromDTO(Category category, CategoryDTO categoryDTO);
}
