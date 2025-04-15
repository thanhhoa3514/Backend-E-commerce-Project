package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.payment.PaymentRequestDTO;
import com.project.e_commerce.dtos.payment.PaymentResponseDTO;
import com.project.e_commerce.services.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponseDTO> createPaymentIntent(@RequestBody PaymentRequestDTO paymentRequest) {
        PaymentResponseDTO response = paymentService.createPaymentIntent(paymentRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<PaymentResponseDTO> confirmPayment(@PathVariable String paymentIntentId) {
        PaymentResponseDTO response = paymentService.confirmPayment(paymentIntentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<PaymentResponseDTO> cancelPayment(@PathVariable String paymentIntentId) {
        PaymentResponseDTO response = paymentService.cancelPayment(paymentIntentId);
        return ResponseEntity.ok(response);
    }
}