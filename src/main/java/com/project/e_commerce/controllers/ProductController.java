package com.project.e_commerce.controllers;

import com.project.e_commerce.components.LocalizationUtils;

import com.project.e_commerce.dtos.product.ProductImageDTO;
import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidParamException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.responses.AuthResponse;
import com.project.e_commerce.responses.ProductListResponse;
import com.project.e_commerce.responses.ProductResponse;
import com.project.e_commerce.services.redis.products.IProductRedisService;
import com.project.e_commerce.services.product.IProductService;


import com.project.e_commerce.utils.MessageKeys;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {
    private final IProductService productService;
    private final LocalizationUtils localizationUtils;
    private final com.project.e_commerce.services.redis.products.IProductRedisService productRedisService;
//    private final SecurityUtils securityUtils;
    
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Create a new product", 
        description = "Create a new product with the provided information. Requires ADMIN role.",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    public ResponseEntity<AuthResponse> createProduct(
            @Valid @RequestBody com.project.e_commerce.dtos.product.ProductDTO productDTO,
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
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
                    .message("Create new product successfully")
                    .status(HttpStatus.CREATED)
                    .data(newProduct)
                    .build());
        } catch (InvalidParamException ex) {
            return ResponseEntity.badRequest().body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
    }

//    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @Operation(
//        summary = "Upload product images",
//        description = "Upload images for a specific product. Requires ADMIN role.",
//        security = { @SecurityRequirement(name = "bearer-key") }
//    )
//
//    }

    @GetMapping("/images/{imageName}")
    @Operation(summary = "View product image", description = "Get a product image by name")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
            }
        } catch (Exception e) {
            log.error("Error viewing image: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    @Operation(summary = "Get products", description = "Get products with filtering and pagination")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0", name = "category_id", required = false) Long categoryId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        try {
            PageRequest pageRequest= PageRequest.of(page, limit,
//                    Sort.by("createdAt").descending()
                    Sort.by("id").ascending()
            );
            int totalPages=0;
            List<ProductResponse> productResponses = productRedisService
                    .getAllProducts(keyword, categoryId, pageRequest);
            if (productResponses!=null && !productResponses.isEmpty()) {
                totalPages = productResponses.getFirst().getTotalPages();
            }
            if(productResponses == null) {
                Page<ProductResponse> productPage = productService
                        .getAllProducts(keyword, categoryId, pageRequest);
                // Lấy tổng số trang
                totalPages = productPage.getTotalPages();
                productResponses = productPage.getContent();
                // Bổ sung totalPages vào các đối tượng ProductResponse
                for (ProductResponse product : productResponses) {
                    product.setTotalPages(totalPages);
                }
                productRedisService.saveAllProducts(
                        productResponses,
                        keyword,
                        categoryId,
                        pageRequest
                );
            }
            Page<ProductResponse> productPage=productService.getAllProducts(keyword, categoryId,pageRequest);

            List<ProductResponse> productResponseLists=productPage.getContent();
            return ResponseEntity.ok(ProductListResponse.
                    builder()
                    .productList(productResponseLists)
                    .totalPages(totalPages)
                    .build());
        } catch (Exception ex) {
            log.error("Error getting products: {}", ex.getMessage(), ex);
            return ResponseEntity.ok(ProductListResponse.
                    builder()
                    .productList(null)
                    .totalPages(0)
                    .build());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Get detailed information of a product by its ID")
    public ResponseEntity<AuthResponse> getProductById(@PathVariable("id") Long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(AuthResponse.builder()
                    .data(ProductResponse.from(existingProduct))
                    .message("Get product details successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build());
        } catch (Exception ex) {
            log.error("Error getting product with ID {}: {}", productId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error retrieving product")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/by-ids")
    @Operation(summary = "Get products by IDs", description = "Get multiple products by their IDs")
    public ResponseEntity<AuthResponse> getProductsByIds(@RequestParam("ids") String ids) {
        try {
            // Split the ids string into a list of Long
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
                    
            List<Product> products = productService.findProductsByIds(productIds);
            List<ProductResponse> productResponses = products.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
                    
            return ResponseEntity.ok(AuthResponse.builder()
                    .data(productResponses)
                    .message("Get products successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body(AuthResponse.builder()
                    .message("Invalid product IDs format")
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        } catch (Exception ex) {
            log.error("Error getting products by IDs: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error retrieving products")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Delete a product", 
        description = "Delete a product by its ID. Requires ADMIN role.",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    public ResponseEntity<AuthResponse> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProduct(id);
            // Clear Redis cache to reflect changes
            productRedisService.clearCache();
            
            return ResponseEntity.ok(AuthResponse.builder()
                    .data(null)
                    .message(String.format("Product with id = %d deleted successfully", id))
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build());
        } catch (Exception ex) {
            log.error("Error deleting product with ID {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error deleting product")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Update a product", 
        description = "Update a product by its ID. Requires ADMIN role.",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    public ResponseEntity<AuthResponse> updateProduct(
            @PathVariable long id,
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result) {
        
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
            Product updatedProduct = productService.updateProduct(id, productDTO);
            // Clear Redis cache to reflect changes
            productRedisService.clearCache();
            
            return ResponseEntity.ok(AuthResponse.builder()
                    .data(ProductResponse.from(updatedProduct))
                    .message("Update product successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build());
        } catch (Exception ex) {
            log.error("Error updating product with ID {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .message("Error updating product")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }

//    @PostMapping("/like/{productId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    @Operation(
//        summary = "Like a product",
//        description = "Like a product by its ID. Requires authentication.",
//        security = { @SecurityRequirement(name = "bearer-key") }
//    )
//    public ResponseEntity<AuthResponse> likeProduct(@PathVariable Long productId) {
//        try {
//            User loginUser = securityUtils.getLoggedInUser();
//            Product likedProduct = productService.likeProduct(loginUser.getId(), productId);
//
//            return ResponseEntity.ok(AuthResponse.builder()
//                    .data(ProductResponse.fromProduct(likedProduct))
//                    .message("Like product successfully")
//                    .status(HttpStatus.OK)
//                    .build());
//        } catch (DataNotFoundException ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
//                    .message(ex.getMessage())
//                    .status(HttpStatus.NOT_FOUND)
//                    .data(null)
//                    .build());
//        } catch (Exception ex) {
//            log.error("Error liking product with ID {}: {}", productId, ex.getMessage(), ex);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
//                    .message("Error liking product")
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .data(null)
//                    .build());
//        }
//    }
//
//    @PostMapping("/unlike/{productId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    @Operation(
//        summary = "Unlike a product",
//        description = "Remove like from a product by its ID. Requires authentication.",
//        security = { @SecurityRequirement(name = "bearer-key") }
//    )
//    public ResponseEntity<AuthResponse> unlikeProduct(@PathVariable Long productId) {
//        try {
//            User loginUser = securityUtils.getLoggedInUser();
//            Product unlikedProduct = productService.unlikeProduct(loginUser.getId(), productId);
//
//            return ResponseEntity.ok(AuthResponse.builder()
//                    .data(ProductResponse.fromProduct(unlikedProduct))
//                    .message("Unlike product successfully")
//                    .status(HttpStatus.OK)
//                    .build());
//        } catch (DataNotFoundException ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AuthResponse.builder()
//                    .message(ex.getMessage())
//                    .status(HttpStatus.NOT_FOUND)
//                    .data(null)
//                    .build());
//        } catch (Exception ex) {
//            log.error("Error unliking product with ID {}: {}", productId, ex.getMessage(), ex);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
//                    .message("Error unliking product")
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .data(null)
//                    .build());
//        }
//    }
//
//    @GetMapping("/favorite-products")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    @Operation(
//        summary = "Get favorite products",
//        description = "Get all products liked by the current user. Requires authentication.",
//        security = { @SecurityRequirement(name = "bearer-key") }
//    )
//    public ResponseEntity<AuthResponse> getFavoriteProducts() {
//        try {
//            User loginUser = securityUtils.getLoggedInUser();
//            List<ProductResponse> favoriteProducts = productService.findFavoriteProductsByUserId(loginUser.getId());
//
//            return ResponseEntity.ok(AuthResponse.builder()
//                    .data(favoriteProducts)
//                    .message("Favorite products retrieved successfully")
//                    .status(HttpStatus.OK)
//                    .build());
//        } catch (Exception ex) {
//            log.error("Error retrieving favorite products: {}", ex.getMessage(), ex);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
//                    .message("Error retrieving favorite products")
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .data(null)
//                    .build());
//        }
//    }
}
