package com.project.e_commerce.services.category;


import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.repositories.CategoryRepository;
import com.project.e_commerce.responses.CategoryResponse;
import com.project.e_commerce.services.category.commands.ICategoryCommandService;
import com.project.e_commerce.services.category.queries.ICategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryCommandService categoryCommandService;
    private final ICategoryQueryService categoryQueryService;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws InvalidParamException {
        return  categoryCommandService.createCategory(categoryDTO);
    }

    @Override
    public Category getCategoryById(long categoryId) {
        return categoryQueryService.getCategoryById(categoryId);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryQueryService.getAllCategories();
    }

    @Override
    public Category updateCategory(long categoryId, CategoryDTO categoryDTO) throws InvalidParamException {
        return categoryCommandService.updateCategory(categoryId, categoryDTO);
    }

    @Override
    public void deleteCategory(long categoryId) {
        categoryCommandService.deleteCategory(categoryId);
    }
}
