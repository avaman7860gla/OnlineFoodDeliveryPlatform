package com.example.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    private Integer orderId;
    private Integer customerId;

    private double amount;

    private String status; // CREATED, PAID, FAILED, REFUNDED
    private String mode;   // UPI, CARD, COD, WALLET

    private String transactionId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String currency;

    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;
    private String paymentUrl;
}
