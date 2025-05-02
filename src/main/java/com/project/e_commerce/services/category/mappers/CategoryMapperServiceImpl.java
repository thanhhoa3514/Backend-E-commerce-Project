package com.project.e_commerce.services.category.mappers;

import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.responses.CategoryResponse;
import org.springframework.stereotype.Service;


@Service
public class CategoryMapperServiceImpl implements  ICategoryMapperService {
    @Override
    public Category mapToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();
    }

    @Override
    public CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    @Override
    public void updateCategoryFromDTO(Category category, CategoryDTO categoryDTO) {
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
    }
}
