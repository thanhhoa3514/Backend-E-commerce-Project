package com.project.e_commerce.controllers;

import com.project.e_commerce.enums.PaymentStatus;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.Payment;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.PaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/webhook/stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    @Transactional
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.warn("⚠️  Webhook error while validating signature.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded": {
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (paymentIntent != null) {
                    onPaymentSucceeded(paymentIntent);
                }
                break;
            }
            case "payment_intent.payment_failed": {
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (paymentIntent != null) {
                    onPaymentFailed(paymentIntent);
                }
                break;
            }
            default:
                log.info("Unhandled event type: {}", event.getType());
        }
        return ResponseEntity.ok("Received");
    }

    private void onPaymentSucceeded(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentIntentId(paymentIntentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            if (payment.getStatus() == PaymentStatus.PENDING) {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                // Optionally update order status
                Order order = payment.getOrder();
                if (order != null) {
                    order.setOrderStatus(com.project.e_commerce.models.enums.OrderStatus.PAID);
                    orderRepository.save(order);
                }
                log.info("Webhook: Updated payment status to SUCCESS for PaymentIntent {}", paymentIntentId);
            }
        } else {
            log.warn("Payment not found for PaymentIntent {}", paymentIntentId);
        }
    }

    private void onPaymentFailed(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentIntentId(paymentIntentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            if (payment.getStatus() == PaymentStatus.PENDING) {
                payment.setStatus(PaymentStatus.FAILED);
                if (paymentIntent.getLastPaymentError() != null) {
                    payment.setFailureCode(paymentIntent.getLastPaymentError().getCode());
                    payment.setFailureMessage(paymentIntent.getLastPaymentError().getMessage());
                }
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                log.info("Webhook: Updated payment status to FAILED for PaymentIntent {}", paymentIntentId);
            }
        } else {
            log.warn("Payment not found for PaymentIntent {}", paymentIntentId);
        }
    }
}
