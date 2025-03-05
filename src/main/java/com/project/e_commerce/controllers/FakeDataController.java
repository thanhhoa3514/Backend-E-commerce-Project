package com.project.e_commerce.controllers;


import com.project.e_commerce.services.FakeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/fake-data")
public class FakeDataController {
    private final FakeDataService fakeDataService;

    @Autowired
    public FakeDataController(FakeDataService fakeDataService) {
        this.fakeDataService = fakeDataService;
    }


//    @PostMapping("products")
    public ResponseEntity<String> getProducts(@RequestParam(defaultValue = "10") int count) {
        return fakeDataService.generateAndSaveFakeProducts(count);
    }
}
