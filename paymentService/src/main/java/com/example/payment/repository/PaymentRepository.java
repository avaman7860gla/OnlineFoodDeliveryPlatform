package com.example.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	Optional<Payment> findTopByOrderIdOrderByPaymentIdDesc(Integer orderId);

    List<Payment> findByCustomerId(Integer customerId);

    List<Payment> findByStatus(String status);

    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
