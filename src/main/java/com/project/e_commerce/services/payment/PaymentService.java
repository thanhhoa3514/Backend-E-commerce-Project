package com.project.e_commerce.services.payment;

import com.project.e_commerce.dtos.payment.PaymentRequestDTO;
import com.project.e_commerce.dtos.payment.PaymentResponseDTO;
import com.project.e_commerce.enums.PaymentStatus;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.PaymentProcessingException;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.Payment;
import com.project.e_commerce.repositories.OrderRepository;
import com.project.e_commerce.repositories.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements IPaymentService {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;


    @Value("${stripe.currency.usd}")
    private String defaultCurrency;
    
    @Value("${stripe.payment.method:card}")
    private String defaultPaymentMethod;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }



    @Override
    @Transactional // Crucial for DB consistency
    public PaymentResponseDTO createPaymentIntent(PaymentRequestDTO paymentRequestDTO) throws DataNotFoundException, PaymentProcessingException {
        // 1. Fetch Order - Good use of orElseThrow
        Order order = orderRepository.findById(paymentRequestDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found with ID: " + paymentRequestDTO.getOrderId()));

        // 2. Idempotency Check - Important, but logic needs review (see points below)
        if (paymentRepository.existsByOrderAndStatus(order, PaymentStatus.SUCCESS) /* ||
             paymentRepository.existsByOrderAndStatus(order, PaymentStatus.PENDING) */) { // Reconsider PENDING check
            log.warn("Payment already successfully processed for order ID: {}", order.getId());

            Payment existingPayment = paymentRepository.findByOrderAndStatus(order, PaymentStatus.SUCCESS)
                    .orElseThrow(() -> new PaymentProcessingException("Inconsistent state: Payment success exists but cannot be retrieved.")); // Should not happen
            log.info("Returning existing successful PaymentIntent {} for order {}", existingPayment.getPaymentIntentId(), order.getId());
            
            // We don't return client secret for existing payments - it was already used
            return PaymentResponseDTO.builder()
                    .paymentIntentId(existingPayment.getPaymentIntentId())
                    .status(existingPayment.getStatus().name())
                    .message("Payment already successfully completed for this order.")
                    .build();
        }

        // 3. Prepare Stripe Request - Good use of builder pattern
        try {
            // Use defaults, consider making them configurable if needed
            String currency = paymentRequestDTO.getCurrency() != null ? paymentRequestDTO.getCurrency().toLowerCase() : "usd";
            String paymentMethodType = paymentRequestDTO.getPaymentMethodId() != null ? paymentRequestDTO.getPaymentMethodType() : "card";

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (order.getTotalPrice() * 100)) // Correct conversion to cents
                    .setCurrency(currency)
                    .addPaymentMethodType(paymentMethodType) // Use addPaymentMethodType for flexibility
                    .putMetadata("order_id", String.valueOf(order.getId())) // Good practice
                    // Consider adding customer ID if you manage Stripe Customers: .setCustomer(stripeCustomerId)
                    // Consider adding description: .setDescription("Order ID: " + order.getId())
                    .build();

            // 4. Create Stripe PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            
            // Get the client secret but DON'T store it
            String clientSecret = paymentIntent.getClientSecret();

            // 5. Persist Local Payment Record - Essential for tracking
            Payment payment = Payment.builder()
                    .order(order)
                    .paymentIntentId(paymentIntent.getId())
                    // Client secret is deliberately not stored for security
                    .amount(order.getTotalPrice()) // Store original amount
                    .currency(paymentIntent.getCurrency()) // Store actual currency used
                    .status(PaymentStatus.PENDING) // Correct initial status
                    .paymentMethodType(paymentIntent.getPaymentMethodTypes().get(0)) // Store actual method used
                    .createdAt(LocalDateTime.now())
                    // .updatedAt(LocalDateTime.now()) // Maybe add updatedAt?
                    .build();
            paymentRepository.save(payment);

            log.info("Created PaymentIntent {} (Status: {}) for order {}", paymentIntent.getId(), paymentIntent.getStatus(), order.getId());

            // 6. Return Response DTO with client secret (only for new payments)
            return PaymentResponseDTO.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .clientSecret(clientSecret) // Return it to client but don't persist
                    .status(payment.getStatus().name()) // Return PENDING status
                    .message("PaymentIntent created successfully. Awaiting client confirmation.")
                    .build();

            // 7. Error Handling - Specific Stripe + General Catch-all is good
        } catch (StripeException e) {
            log.error("Stripe error creating PaymentIntent for order {}: Status={}, Code={}, Message={}",
                    order.getId(), e.getStatusCode(), e.getCode(), e.getMessage(), e);
            // Consider mapping specific Stripe errors (e.g., card_error) if different handling is needed
            throw new PaymentProcessingException("Payment provider error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error creating PaymentIntent for order {}: {}", order.getId(), e.getMessage(), e);
            throw new PaymentProcessingException("An unexpected error occurred during payment initiation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentStatus(String paymentIntentId) {
        try {
            // First check our local database
            Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                    .orElseThrow(() -> new DataNotFoundException("Payment not found with ID: " + paymentIntentId));
            
            // For the most up-to-date status, also check with Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            // Update local record if the status has changed
            PaymentStatus stripeStatus = mapStripeStatus(paymentIntent.getStatus());
            if (payment.getStatus() != stripeStatus) {
                payment.setStatus(stripeStatus);
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
            }
            
            return PaymentResponseDTO.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .status(payment.getStatus().name())
                    .message("Payment status retrieved successfully")
                    .build();
            
        } catch (StripeException e) {
            log.error("Stripe error retrieving PaymentIntent {}: {}", paymentIntentId, e.getMessage(), e);
            throw new PaymentProcessingException("Payment provider error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error retrieving PaymentIntent {}: {}", paymentIntentId, e.getMessage(), e);
            throw new PaymentProcessingException("An unexpected error occurred while retrieving payment status.", e);
        }
    }
    
    // Helper method to map Stripe status to our enum
    private PaymentStatus mapStripeStatus(String stripeStatus) {
        switch (stripeStatus) {
            case "succeeded":
                return PaymentStatus.SUCCESS;
            case "requires_action":
                return PaymentStatus.REQUIRES_ACTION;
            case "requires_payment_method":
                return PaymentStatus.FAILED;
            case "canceled":
                return PaymentStatus.CANCELED;
            default:
                return PaymentStatus.PENDING;
        }
    }

    @Override
    @Transactional
    public PaymentResponseDTO confirmPayment(String paymentIntentId) {
        try {
            // Retrieve the PaymentIntent from Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            // Confirm the PaymentIntent (if it's not already confirmed)
            if (!"succeeded".equals(paymentIntent.getStatus())) {
                paymentIntent = paymentIntent.confirm();
            }
            
            // Update the local payment record
            Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                    .orElseThrow(() -> new DataNotFoundException("Payment not found with ID: " + paymentIntentId));
            
            // Update payment status based on Stripe response
            payment.setStatus(mapStripeStatus(paymentIntent.getStatus()));
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            
            return PaymentResponseDTO.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .status(payment.getStatus().name())
                    .message("Payment " + payment.getStatus().name().toLowerCase())
                    .build();
            
        } catch (StripeException e) {
            log.error("Stripe error confirming PaymentIntent {}: {}", paymentIntentId, e.getMessage(), e);
            throw new PaymentProcessingException("Payment provider error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error confirming PaymentIntent {}: {}", paymentIntentId, e.getMessage(), e);
            throw new PaymentProcessingException("An unexpected error occurred while confirming payment.", e);
        }
    }

    @Override
    @Transactional
    public PaymentResponseDTO cancelPayment(String paymentIntentId) {
        try {
            // Retrieve the PaymentIntent from Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            // Cancel the PaymentIntent
            PaymentIntent canceledIntent = paymentIntent.cancel();
            
            // Update the local payment record
            Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                    .orElseThrow(() -> new DataNotFoundException("Payment not found with ID: " + paymentIntentId));
            
            payment.setStatus(PaymentStatus.CANCELED);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            
            return PaymentResponseDTO.builder()
                    .paymentIntentId(canceledIntent.getId())
                    .status(PaymentStatus.CANCELED.name())
                    .message("Payment canceled successfully")
                    .build();
            
        } catch (StripeException e) {
            log.error("Stripe error canceling PaymentIntent {}: {}", paymentIntentId, e.getMessage(), e);
            throw new PaymentProcessingException("Payment provider error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error canceling PaymentIntent {}: {}", paymentIntentId, e.getMessage(), e);
            throw new PaymentProcessingException("An unexpected error occurred while canceling payment.", e);
        }
    }

    // --- CRITICAL MISSING PIECES ---
    /*
    We absolutely need handlers for Stripe Webhooks to update the payment status.

    @Transactional
    public void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntent.getId())
                .orElseThrow(() -> new DataNotFoundException("Payment not found for intent ID: " + paymentIntent.getId()));

        if (payment.getStatus() == PaymentStatus.PENDING) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setUpdatedAt(LocalDateTime.now());
            // Potentially update associated Order status here as well
            // Order order = payment.getOrder();
            // order.setStatus(OrderStatus.PAID); // Or similar
            // orderRepository.save(order);
            paymentRepository.save(payment);
            log.info("Webhook: Updated payment status to SUCCESS for PaymentIntent {}", paymentIntent.getId());
        } else {
            log.warn("Webhook: Received payment_intent.succeeded for PaymentIntent {} which is already in status {}",
                    paymentIntent.getId(), payment.getStatus());
        }
    }

    @Transactional
    public void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
         Payment payment = paymentRepository.findByPaymentIntentId(paymentIntent.getId())
                .orElseThrow(() -> new DataNotFoundException("Payment not found for intent ID: " + paymentIntent.getId()));

        if (payment.getStatus() == PaymentStatus.PENDING) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureCode(paymentIntent.getLastPaymentError() != null ? paymentIntent.getLastPaymentError().getCode() : null);
            payment.setFailureMessage(paymentIntent.getLastPaymentError() != null ? paymentIntent.getLastPaymentError().getMessage() : null);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
             log.info("Webhook: Updated payment status to FAILED for PaymentIntent {}", paymentIntent.getId());
        } else {
             log.warn("Webhook: Received payment_intent.payment_failed for PaymentIntent {} which is already in status {}",
                    paymentIntent.getId(), payment.getStatus());
        }
    }
    */

    // --- Other Potential Methods ---
    // - getPaymentStatus(Long orderId) -> Optional<PaymentResponseDTO>
    // - createRefund(String paymentIntentId, Long amount) // Requires careful logic
}