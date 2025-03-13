package com.project.e_commerce.services.category.queries;

import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.repositories.CategoryRepository;
import com.project.e_commerce.responses.CategoryResponse;
import com.project.e_commerce.services.category.mappers.ICategoryMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements ICategoryQueryService {
    private final CategoryRepository categoryRepository;
    private final ICategoryMapperService categoryMapperService;
    @Override
    public Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapperService::mapToCategoryResponse)
                .collect(Collectors.toList());
    }
}
