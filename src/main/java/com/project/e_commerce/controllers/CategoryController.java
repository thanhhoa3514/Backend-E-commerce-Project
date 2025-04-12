package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.CategoryDTO;

import com.project.e_commerce.responses.CategoryResponse;
import com.project.e_commerce.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody
                                                CategoryDTO categoryDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            List<String> errorMessages=bindingResult.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);

        }
        try {
            categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok("Category added: " + categoryDTO.getName());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    @GetMapping("")//http://localhost:8080/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<CategoryResponse> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PutMapping("{id}")
    public ResponseEntity<String> updateCategory(@Valid @PathVariable Long id,@RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.updateCategory(id,categoryDTO);
            return ResponseEntity.ok("Category updated successfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted");
    }
}
