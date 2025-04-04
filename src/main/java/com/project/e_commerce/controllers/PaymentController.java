package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.payment.PaymentRequest;
import com.project.e_commerce.dtos.payment.PaymentResponse;
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
    public ResponseEntity<PaymentResponse> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.createPaymentIntent(paymentRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<PaymentResponse> confirmPayment(@PathVariable String paymentIntentId) {
        PaymentResponse response = paymentService.confirmPayment(paymentIntentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable String paymentIntentId) {
        PaymentResponse response = paymentService.cancelPayment(paymentIntentId);
        return ResponseEntity.ok(response);
    }
}