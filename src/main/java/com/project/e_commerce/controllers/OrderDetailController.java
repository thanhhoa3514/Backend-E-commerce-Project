package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping()
    public ResponseEntity<?> createOrderDetailForOrder(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            return ResponseEntity.ok().body("hihi");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getOrderDetailForOrderById(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok().body("get order detail by id: " + id);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsForOrderById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok().body("get order detail by id: " + orderId);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateOrderDetailForOrderById(@Valid @RequestBody OrderDetailDTO orderDetailDTO, @PathVariable("id") Long id) {
        return ResponseEntity.ok().body("update order detail by id: " + id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrderDetailForOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body("delete order detail by id: " + id);
    }

}
