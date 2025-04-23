package com.project.e_commerce.controllers;

import com.project.e_commerce.components.LocalizationUtils;
import com.project.e_commerce.components.converters.CategoryMessageConverter;
import com.project.e_commerce.dtos.CategoryDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Category;
import com.project.e_commerce.responses.AuthResponse;
import com.project.e_commerce.responses.CategoryResponse;
import com.project.e_commerce.services.category.ICategoryService;
import com.project.e_commerce.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
@Slf4j
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {
    private final ICategoryService categoryService;
    private final LocalizationUtils localizationUtils;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Create a new category", 
        description = "Create a new category with the provided information. Requires ADMIN role.",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    public ResponseEntity<AuthResponse> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        
        if(result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessage = localizationUtils.getLocalizedMessage(error.getDefaultMessage());
                errorMap.put(fieldName, errorMessage);
            }
            
            return ResponseEntity.badRequest().body(AuthResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED))
                    .status(HttpStatus.BAD_REQUEST)
                    .data(errorMap)
                    .build());
        }
        
        try {
            Category category = categoryService.createCategory(categoryDTO);
            
            // Send to Kafka asynchronously with error handling
            try {
                this.kafkaTemplate.send("insert-a-category", category);//producer
                this.kafkaTemplate.setMessageConverter(new CategoryMessageConverter());
            } catch (KafkaException ex) {
                // Log error but don't block the API response
                log.error("Kafka error when sending category: {}", ex.getMessage());
            }
            
            return ResponseEntity.ok().body(AuthResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY))
                    .status(HttpStatus.OK)
                    .data(category)
                    .build());
        } catch (InvalidParamException ex) {
            return ResponseEntity.badRequest().body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthResponse.builder()
                    .message("Category name already exists")
                    .status(HttpStatus.CONFLICT)
                    .data(null)
                    .build());
        } catch (Exception ex) {
            log.error("Error creating category: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Internal server error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("")
    @Operation(
        summary = "Get all categories", 
        description = "Retrieves all categories"
    )
    public ResponseEntity<AuthResponse> getAllCategories() {
        try {
            List<CategoryResponse> categories = categoryService.getAllCategories();
            
            // Send to Kafka with error handling
            try {
                CompletableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send("get-all-categories", categories);
                future.whenComplete((sendResult, ex) -> {
                    if (ex == null) {
                        log.debug("Categories list sent to Kafka, size: {}", categories.size());
                    } else {
                        log.error("Failed to send categories to Kafka: {}", ex.getMessage());
                    }
                });
            } catch (KafkaException ex) {
                log.error("Kafka error when sending categories list: {}", ex.getMessage());
            }
            
            return ResponseEntity.ok(AuthResponse.builder()
                    .message("Get list of categories successfully")
                    .status(HttpStatus.OK)
                    .data(categories)
                    .build());
        } catch (Exception ex) {
            log.error("Error retrieving categories: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error retrieving categories")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get category by ID", 
        description = "Retrieves a specific category by its ID"
    )
    public ResponseEntity<AuthResponse> getCategoryById(
            @PathVariable("id") Long categoryId
    ) {
        try {
            Category existingCategory = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(AuthResponse.builder()
                    .data(existingCategory)
                    .message("Get category information successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build());
        } catch (Exception ex) {
            log.error("Error retrieving category with ID {}: {}", categoryId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error retrieving category")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Update a category", 
        description = "Update an existing category by ID. Requires ADMIN role.",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    public ResponseEntity<AuthResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            Map<String, String> errorMap = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    error -> localizationUtils.getLocalizedMessage(error.getDefaultMessage())
                ));
            
            return ResponseEntity.badRequest().body(AuthResponse.builder()
                    .message("Validation errors")
                    .status(HttpStatus.BAD_REQUEST)
                    .data(errorMap)
                    .build());
        }
        
        try {
            Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(AuthResponse.builder()
                    .data(updatedCategory)
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build());
        } catch (InvalidParamException ex) {
            return ResponseEntity.badRequest().body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build()); 
        } catch (Exception ex) {
            log.error("Error updating category with ID {}: {}", id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error updating category")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Delete a category", 
        description = "Delete a category by ID. Requires ADMIN role.",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    public ResponseEntity<AuthResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(AuthResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY))
                    .status(HttpStatus.OK)
                    .data(null)
                    .build());
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build());
        } catch (Exception ex) {
            log.error("Error deleting category with ID {}: {}", id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error deleting category")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }
}
