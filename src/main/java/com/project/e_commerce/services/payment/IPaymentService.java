package com.project.e_commerce.services.payment;

import com.project.e_commerce.dtos.payment.PaymentRequestDTO;
import com.project.e_commerce.dtos.payment.PaymentResponseDTO;

public interface IPaymentService {
    PaymentResponseDTO createPaymentIntent(PaymentRequestDTO paymentRequestDTO);
    
    PaymentResponseDTO getPaymentStatus(String paymentIntentId);
    
    PaymentResponseDTO confirmPayment(String paymentIntentId);
    
    PaymentResponseDTO cancelPayment(String paymentIntentId);
}
