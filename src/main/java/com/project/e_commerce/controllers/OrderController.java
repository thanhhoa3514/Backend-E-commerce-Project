package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.OrderDTO;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.services.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;
    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid  @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        try {

            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult
                        .getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            OrderResponse orderResponse=orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(orderResponse);
        }catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/user/{user_id}")
    //GET http://localhost:3000/api/v1/orders/4
    public ResponseEntity<?> getOrderByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<Order> responseList=orderService.getAllOrdersByUserId(userId);
            return ResponseEntity.ok().body(responseList);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    //GET http://localhost:3000/api/v1/orders/4
    public ResponseEntity<?> getOrderByOrderId(@Valid @PathVariable("id") Long orderId) {
        try {
            Order orderResponse=orderService.getOrderById(orderId);
            return ResponseEntity.ok().body(orderResponse);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // PUT http://localhost:8080/api/v1/orders/4
    public ResponseEntity<?> updateOrderByIdOfThisOrder(@Valid
                                                            @RequestBody OrderDTO orderDTO,
                                                        @Valid @PathVariable Long id) {
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
