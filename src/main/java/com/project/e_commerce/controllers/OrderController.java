package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid  @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        try {

            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok().body("Order is created successfully");
        }catch (Exception e) {
//            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{user_id}")
    //GET http://localhost:3000/api/v1/orders/4
    public ResponseEntity<?> getOrderByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            return ResponseEntity.ok().body("List of orders by user_id");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // PUT http://localhost:8080/api/v1/orders/4
    public ResponseEntity<?> updateOrderByIdOfThisOrder(@Valid @RequestBody OrderDTO orderDTO, @PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(orderDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderByIdOfThisOrder(@Valid @PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().body("Order deleted");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
