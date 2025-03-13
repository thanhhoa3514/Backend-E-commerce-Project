package com.project.e_commerce.services.category.queries;

import com.project.e_commerce.models.Category;
import com.project.e_commerce.responses.CategoryResponse;

import java.util.List;

public interface ICategoryQueryService {
    Category getCategoryById(long categoryId);
    List<CategoryResponse> getAllCategories();
}
