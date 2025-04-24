package com.project.e_commerce.controllers;

import com.project.e_commerce.components.LocalizationUtils;
import com.project.e_commerce.dtos.order.OrderDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.responses.OrderResponse;
import com.project.e_commerce.responses.ResponseObject;
import com.project.e_commerce.services.order.IOrderService;
import com.project.e_commerce.services.order.OrderService;
import com.project.e_commerce.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;


    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createOrder(@Valid  @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        try {

            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult
                        .getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        ResponseObject.builder()
                                .message(String.join(";", errorMessages))
                                .status(HttpStatus.BAD_REQUEST)
                                .build());
            }

            OrderResponse orderResponse=orderService.createOrder(orderDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create order successfully")
                    .data(orderResponse)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e) {

            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }

    }

    @GetMapping("/user/{user_id}")
    //GET http://localhost:3000/api/v1/orders/4
    public ResponseEntity<ResponseObject> getOrderByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<OrderResponse> orderResponses=orderService.getAllOrdersByUserId(userId);
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Get list of orders successfully")
                    .data(orderResponses)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e) {
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @GetMapping("/{id}")
    //GET http://localhost:3000/api/v1/orders/4
    public ResponseEntity<ResponseObject> getOrderByOrderId(@Valid @PathVariable("id") Long orderId) {
        try {
            OrderResponse orderResponse=orderService.getOrderById(orderId);
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Get list of orders successfully")
                    .data(orderResponse)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e) {
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //công việc của admin
    // PUT http://localhost:8080/api/v1/orders/4
    public ResponseEntity<?> updateOrderByIdOfThisOrder(@Valid
                                                            @RequestBody OrderDTO orderDTO,
                                                        @Valid @PathVariable Long id) {
        try {
            OrderResponse updatedOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Update order successfully")
                    .data(updatedOrder)
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            @PathVariable Long id) {
        try {
            OrderResponse updatedOrder = orderService.partialUpdateOrder(id, orderDTO);
            return ResponseEntity.ok().body(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteOrderByIdOfThisOrder(@Valid @PathVariable("id") Long id) {
        try {
            orderService.deleteOrder(id);
            String message = localizationUtils.getLocalizedMessage(
                    MessageKeys.DELETE_ORDER_SUCCESSFULLY, id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message(message)
                            .build()
            );
        }catch (DataNotFoundException e) {
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Data not found")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestParam String status) {
        try {
            orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Data not found")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Data not found"+ e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Invalid status: "+ e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject
                    .builder()
                    .message("Erroring updating status "+ e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
    @GetMapping("/get-orders-by-keyword")
    public ResponseEntity<ResponseObject> getOrdersByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        Page<OrderResponse> orderPage = orderService
                .getOrdersByKeyword(keyword, pageRequest)
                .map(OrderResponse::from);
        // Lấy tổng số trang
        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orderResponses = orderPage.getContent();
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get orders successfully")
                .status(HttpStatus.OK)
                .data(orderResponses)
                .build());
    }

}
