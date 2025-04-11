package com.project.e_commerce.services.fake_data;


import com.project.e_commerce.dtos.product.ProductDTO;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.services.product.ProductService;
import com.project.e_commerce.utils.FakeDataGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service

public class FakeDataService {
    private final FakeDataGeneratorUtil fakeDataGeneratorUtil;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
    private final ProductService productService;

    @Autowired
    public FakeDataService(
            FakeDataGeneratorUtil fakeDataGeneratorUtil,

            ProductRepository productRepository, ProductService productService
    ) {
        this.fakeDataGeneratorUtil = fakeDataGeneratorUtil;
        this.productService = productService;
    }
    public ResponseEntity<String> generateAndSaveFakeProducts(int count) {
        for (int i = 0; i < count; i++) {
            ProductDTO productDTO = fakeDataGeneratorUtil.generateFakeProductDTO();

            if (productService.exitsByName(productDTO.getName())) {
                continue; // Bỏ qua nếu sản phẩm đã tồn tại
            }

            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Products created successfully");
    }

//    public List<User> generateAndSaveFakeUsers(int count) {
//        List<User> fakeUsers = fakeDataGeneratorUtil.generateFakeUsers(count);
//        return userRepository.saveAll(fakeUsers);
//    }
}
