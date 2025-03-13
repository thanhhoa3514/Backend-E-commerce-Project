package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.OrderDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.User;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.services.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class OrderController {
    private final OrderService orderService;
    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid  @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult
                        .getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            orderDTO.setUserId(currentUser.getId());
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
            List<OrderResponse> responseList=orderService.getAllOrdersByUserId(userId);
            return ResponseEntity.ok().body(responseList);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    //GET http://localhost:3000/api/v1/orders/4
    public ResponseEntity<?> getOrderByOrderId(@Valid @PathVariable("id") Long orderId) {
        try {
            OrderResponse orderResponse=orderService.getOrderById(orderId);
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
            OrderResponse updatedOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok().body(updatedOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            @PathVariable Long id) {

        try {
            OrderResponse updatedOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok().body(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderByIdOfThisOrder(@Valid @PathVariable("id") Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().body("Order deleted successfully");
        }catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting order: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestParam String status) {
        try {
            orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok().body("Order status updated successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating order status: " + e.getMessage());
        }
    }

}
