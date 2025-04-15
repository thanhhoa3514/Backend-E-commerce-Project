package com.project.e_commerce.repositories;

import com.project.e_commerce.enums.PaymentStatus;
import com.project.e_commerce.models.Order;
import com.project.e_commerce.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);

    Optional<Payment> findByOrderAndStatus(Order order, PaymentStatus status);

    boolean existsByOrderAndStatus(Order order, PaymentStatus status);
}
