package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.ProductDTO;

import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.ProductImage;
import com.project.e_commerce.responses.ProductListResponse;
import com.project.e_commerce.responses.ProductResponse;
import com.project.e_commerce.services.product_image.ProductImageService;
import com.project.e_commerce.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;




import java.util.List;


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
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {

        try {

            Product product=productService.getProductById(id);
            return ResponseEntity.ok(ProductResponse.from(product));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

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
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {

        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted"+id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateProductByIdProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDTO productDTO) {
        try {
            Product updatedProduct=productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(ProductResponse.from(updatedProduct));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}
