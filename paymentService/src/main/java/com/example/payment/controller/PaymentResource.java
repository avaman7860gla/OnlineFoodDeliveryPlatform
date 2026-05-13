package com.example.payment.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.entity.Payment;
import com.example.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentResource {

    private final PaymentService service;

    @PostMapping("/process")
    public ResponseEntity<Payment> process(@RequestBody Payment payment) {
        return ResponseEntity.ok(service.processPayment(payment));
    }
    
    @PostMapping("/create-order")
    public Payment createOrder(@RequestParam Integer orderId,
                               @RequestParam Integer customerId,
                               @RequestParam double amount) {

        return service.createRazorpayOrder(orderId, customerId, amount);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(service.getByOrder(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(service.getByCustomer(customerId));
    }

    @PostMapping("/refund/{id}")
    public ResponseEntity<?> refund(@PathVariable Integer id) {
        return ResponseEntity.ok(service.refundPayment(id));
    }

    // 🔥 WALLET

    @GetMapping("/wallet/{customerId}")
    public ResponseEntity<Double> getBalance(@PathVariable Integer customerId) {
        return ResponseEntity.ok(service.getWalletBalance(customerId));
    }

    @PostMapping("/wallet/add")
    public ResponseEntity<?> add(@RequestParam Integer customerId,
                                @RequestParam double amount) {
        service.addToWallet(customerId, amount);
        return ResponseEntity.ok("Added");
    }

    @PostMapping("/wallet/pay")
    public ResponseEntity<?> pay(@RequestParam Integer customerId,
                                @RequestParam double amount) {
        return ResponseEntity.ok(service.payFromWallet(customerId, amount));
    }
    
    @PostMapping("/verify")
    public ResponseEntity<Object> verify(@RequestParam String razorpayOrderId,
                                           @RequestParam String razorpayPaymentId) {

        return ResponseEntity.ok(
                service.verifyPayment(razorpayOrderId, razorpayPaymentId)
        );
    }
    
    @PostMapping("/create-link")
    public ResponseEntity<?> createLink(@RequestParam Integer orderId,
                                        @RequestParam Integer customerId,
                                        @RequestParam double amount) {

        return ResponseEntity.ok(
            service.createPaymentLink(orderId, customerId, amount)
        );
    }
    
    @GetMapping("/callback")
    public String paymentCallback(@RequestParam Map<String, String> params) {

        String paymentId = params.get("razorpay_payment_id");
        String linkId = params.get("razorpay_payment_link_id");
        String status = params.get("razorpay_payment_link_status");

        System.out.println("CALLBACK RECEIVED: " + params);

        return service.handlePaymentCallback(linkId, paymentId, status);
    }
}
