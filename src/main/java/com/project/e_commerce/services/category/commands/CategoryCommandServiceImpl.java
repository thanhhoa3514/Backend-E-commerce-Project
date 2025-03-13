package com.project.e_commerce.services.category.commands;

import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.repositories.CategoryRepository;
import com.project.e_commerce.services.category.mappers.ICategoryMapperService;
import com.project.e_commerce.services.category.validation.CategoryValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements ICategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final ICategoryMapperService categoryMapperService;
    private final CategoryValidationService categoryValidationService;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws InvalidParamException {
        categoryValidationService.validateName(categoryDTO.getName());
        Category category = categoryMapperService.mapToCategory(categoryDTO);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(long categoryId, CategoryDTO categoryDTO) throws InvalidParamException {
        categoryValidationService.validateName(categoryDTO.getName());
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        categoryMapperService.updateCategoryFromDTO(existingCategory, categoryDTO);
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}
