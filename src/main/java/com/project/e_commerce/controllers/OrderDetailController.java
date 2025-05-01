package com.project.e_commerce.controllers;

import com.project.e_commerce.components.LocalizationUtils;
import com.project.e_commerce.dtos.order.OrderDetailDTO;
import com.project.e_commerce.responses.ApiResponse;
import com.project.e_commerce.responses.OrderDetailResponse;
import com.project.e_commerce.responses.ResponseObject;
import com.project.e_commerce.services.orderdetails.OrderDetailService;
import com.project.e_commerce.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetailResponse orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(
                    ApiResponse.builder().message("Create order detail successfully").status(HttpStatus.CREATED.value()).data(orderDetail).build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().message("Create order detail successfully").status(HttpStatus.CREATED.value()).data(null).build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            OrderDetailResponse orderDetail = orderDetailService.getOrderDetailById(id);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .message("Get order detail successfully")
                            .status(HttpStatus.OK.value())
                            .data(orderDetail)
                            .build()

            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") Long orderId) {
        try {
            List<OrderDetailResponse> orderDetails = orderDetailService.getAllOrderDetailsByOrderId(orderId);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .message("Get order details by orderId successfully")
                            .status(HttpStatus.OK.value())
                            .data(orderDetails)
                            .build()

            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            @PathVariable("id") Long id) {
        try {
            OrderDetailResponse updatedOrderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(
                    ResponseObject
                            .builder()
                            .data(updatedOrderDetail)
                            .message("Update order detail successfully")
                            .status(HttpStatus.OK)
                            .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("id") Long id) {
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                    .message(localizationUtils
                            .getLocalizedMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
