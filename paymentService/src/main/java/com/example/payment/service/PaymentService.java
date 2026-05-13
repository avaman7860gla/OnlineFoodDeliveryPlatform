package com.example.payment.service;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.example.payment.entity.Payment;

public interface PaymentService {

    Payment processPayment(Payment payment);

    Payment getByOrder(Integer orderId);

    List<Payment> getByCustomer(Integer customerId);

    Payment refundPayment(Integer paymentId);

    double getWalletBalance(Integer customerId);

    void addToWallet(Integer customerId, double amount);

    Payment payFromWallet(Integer customerId, double amount);

    void updatePaymentStatus(Integer paymentId, String status);

	Payment createRazorpayOrder(Integer orderId, Integer customerId, double amount);

	Object verifyPayment(String razorpayOrderId, String razorpayPaymentId);

	Payment createPaymentLink(Integer orderId, Integer customerId, double amount);

	String handlePaymentCallback(String linkId, String paymentId, String status);
	
}
