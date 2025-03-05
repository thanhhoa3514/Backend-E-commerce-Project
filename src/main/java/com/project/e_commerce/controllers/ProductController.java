package com.project.e_commerce.controllers;

import ch.qos.logback.core.util.StringUtil;
import com.github.javafaker.Faker;
import com.project.e_commerce.dtos.ProductDTO;

import com.project.e_commerce.dtos.ProductImageDTO;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.responses.ProductListResponse;
import com.project.e_commerce.responses.ProductResponse;
import com.project.e_commerce.services.ProductImageService;
import com.project.e_commerce.services.ProductService;
import com.project.e_commerce.utils.FileValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductImageService productImageService;


    @GetMapping()
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam("page") int page
            , @RequestParam("limit") int limit) {

        PageRequest pageRequest= PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage=productService.getAllProducts(pageRequest);
        int totalPages=productPage.getTotalPages();
        List<ProductResponse> productResponseLists=productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.
                builder()
                        .productList(productResponseLists)
                        .totalPages(totalPages)
                .build());
    }


    @GetMapping("{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String id) {

        return ResponseEntity.ok("Product found"+id);

    }



    @PostMapping(value = "")
    public ResponseEntity<?> addNewProduct(@Valid @RequestBody
                                               ProductDTO productDTO,
                                           BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {

                List<String> errorMessages = bindingResult
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();


                return ResponseEntity.badRequest().body(errorMessages);

            }
            Product product=productService.createProduct(productDTO);


            return ResponseEntity.status(HttpStatus.CREATED).body(product);

        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImagesForProduct(
            @PathVariable("id") Long productId,
            @RequestPart("files") List<MultipartFile> multipartFiles

            ) {

        try {

            List<ProductImage> productImages = productImageService.uploadProductImages(productId, multipartFiles);
            return ResponseEntity.ok(productImages);

        } catch (Exception ex) {
            return ResponseEntity.status(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
            ).body(ex.getMessage());


        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") String id) {

        return ResponseEntity.status(HttpStatus.OK).body("Product deleted"+id);

    }

//    @PostMapping("fake-data")
//    public ResponseEntity<String> fakeData() {
//        Faker faker = new Faker();
//        for(int i =0;i<1000000;i++){
//            String productName = faker.commerce().productName();
//            if(productService.exitsByName(productName)){
//                continue;
//            }
//            ProductDTO productDTO=ProductDTO
//
//                    .builder()
//                    .name(productName)
//                    .price(faker.number().numberBetween(10,90_000_000))
//                    .description(faker.lorem().sentence())
//                    .categoryId((long)faker.number().numberBetween(1,3))
//                    .quantity(faker.number().numberBetween(1,1000))
//
//                    .build();
//            try {
//                productService.createProduct(productDTO);
//            }catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//            }
//        }
//        return ResponseEntity.status(HttpStatus.OK).body("Products created successfully");
//    }
}
