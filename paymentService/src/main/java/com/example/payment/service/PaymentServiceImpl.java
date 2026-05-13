package com.example.payment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.payment.config.RazorpayConfig;
import com.example.payment.dto.Notification;
import com.example.payment.entity.Payment;
import com.example.payment.entity.Wallet;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.repository.WalletRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import com.razorpay.PaymentLink;

import lombok.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final WalletRepository walletRepo;
    
    private final RazorpayConfig config;
    
    private final RestTemplate restTemplate;
    
    @Override
    public Payment createRazorpayOrder(Integer orderId, Integer customerId, double amount) {

        try {
            System.out.println("DEBUG: Initializing Razorpay with Key: " + config.getKey());
            RazorpayClient client = new RazorpayClient(config.getKey(), config.getSecret());

            JSONObject options = new JSONObject();
            // 💡 IMPORTANT: Amount must be an Integer/Long in Paisa
            int amountInPaisa = (int) Math.round(amount * 100);
            options.put("amount", amountInPaisa); 
            options.put("currency", "INR");
            options.put("receipt", "order_" + orderId);

            System.out.println("DEBUG: Creating Razorpay Order with options: " + options);
            Order razorOrder = client.orders.create(options);

            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setCustomerId(customerId);
            payment.setAmount(amount);

            payment.setRazorpayOrderId(razorOrder.get("id"));
            payment.setStatus("CREATED");
            payment.setCurrency("INR");

            System.out.println("DEBUG: Razorpay Order Created: " + razorOrder.get("id"));
            return paymentRepo.save(payment);

        } catch (Exception e) {
            System.err.println("CRITICAL: Razorpay Exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Razorpay error: " + e.getMessage());
        }
    }
    
    @Override
    public Payment verifyPayment(String razorpayOrderId,
                                 String razorpayPaymentId) {

        Payment payment = paymentRepo.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());

        Payment saved = paymentRepo.save(payment);

        // 🔥 CALL ORDER SERVICE
        String url = "http://localhost:8085/orders/status"
                + "?id=" + payment.getOrderId()
                + "&status=PAID";

        restTemplate.put(url, null);

        return saved;
    }

    @Override
    public Payment processPayment(Payment payment) {

        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setCurrency("INR");

        return paymentRepo.save(payment);
    }

    @Override
    public Payment getByOrder(Integer orderId) {
        return paymentRepo.findTopByOrderIdOrderByPaymentIdDesc(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
    @Override
    public List<Payment> getByCustomer(Integer customerId) {
        return paymentRepo.findByCustomerId(customerId);
    }

    @Override
    public Payment refundPayment(Integer paymentId) {

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus("REFUNDED");
        payment.setRefundedAt(LocalDateTime.now());

        return paymentRepo.save(payment);
    }

    // 🔥 WALLET

    @Override
    public double getWalletBalance(Integer customerId) {
        return walletRepo.findByCustomerId(customerId)
                .map(Wallet::getBalance)
                .orElse(0.0);
    }

    @Override
    public void addToWallet(Integer customerId, double amount) {

        Wallet wallet = walletRepo.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setCustomerId(customerId);
                    w.setBalance(0);
                    w.setCreatedAt(LocalDateTime.now());
                    return w;
                });

        wallet.setBalance(wallet.getBalance() + amount);

        walletRepo.save(wallet);
    }

    @Override
    public Payment payFromWallet(Integer customerId, double amount) {

        Wallet wallet = walletRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepo.save(wallet);

        Payment payment = new Payment();
        payment.setCustomerId(customerId);
        payment.setAmount(amount);
        payment.setMode("WALLET");

        return processPayment(payment);
    }

    @Override
    public void updatePaymentStatus(Integer paymentId, String status) {

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(status);
        paymentRepo.save(payment);
    }

    @Override
    public Payment createPaymentLink(Integer orderId, Integer customerId, double amount) {

        try {
            RazorpayClient client = new RazorpayClient(config.getKey(), config.getSecret());

            JSONObject options = new JSONObject();
            options.put("amount", (int)(amount * 100)); // paisa
            options.put("currency", "INR");

            options.put("description", "Food Order Payment");

            JSONObject customer = new JSONObject();
            customer.put("name", "Customer");
            customer.put("email", "test@gmail.com"); // optional
            options.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            options.put("notify", notify);

            options.put("callback_url", "http://localhost:8086/payment/callback");
            options.put("callback_method", "get");

            PaymentLink link = client.paymentLink.create(options);
            String linkId = link.get("id");
            String url = link.get("short_url");   // 🔥 IMPORTANT

            System.out.println("PAY HERE - " + url);

            // 🔥 SAVE IN DB
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setCustomerId(customerId);
            payment.setAmount(amount);
            payment.setStatus("CREATED");

            payment.setTransactionId(linkId);
            payment.setPaymentUrl(url);
            payment.setCurrency("INR");

            return paymentRepo.save(payment);

        } catch (Exception e) {
            throw new RuntimeException("Razorpay link error: " + e.getMessage());
        }
    }
    
    @Override
    public String handlePaymentCallback(String linkId, String paymentId, String status) {

        Payment payment = paymentRepo.findByTransactionId(linkId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if ("paid".equalsIgnoreCase(status)) {

            payment.setStatus("PAID");
            payment.setRazorpayPaymentId(paymentId);

            paymentRepo.save(payment);

            // 🔔 SEND NOTIFICATION HERE
            try {
                String notifyUrl = "http://localhost:8088/notifications/send";

                Notification n = new Notification();
                n.setRecipientId(payment.getCustomerId());
                n.setType("PAYMENT");
                n.setTitle("Payment Successful");
                n.setMessage("Payment for Order #" + payment.getOrderId() + " completed");
                n.setChannel("APP");
                n.setRelatedId(payment.getOrderId());
                n.setRelatedType("ORDER");

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer <token>"); // temporary

                HttpEntity<Notification> req = new HttpEntity<>(n, headers);

                restTemplate.exchange(
                        notifyUrl,
                        HttpMethod.POST,
                        req,
                        String.class
                );

                System.out.println("✅ Payment notification sent");

            } catch (Exception e) {
                System.out.println("❌ Notification failed: " + e.getMessage());
            }

            // 🔥 OPTIONAL: Update Order Service
            try {
                String url = "http://localhost:8085/orders/update-status/"
                        + payment.getOrderId() + "?status=PAID";

                restTemplate.postForObject(url, null, String.class);
            } catch (Exception e) {
                System.out.println("Order update failed: " + e.getMessage());
            }

            return "Payment SUCCESS";
        }

        return "Payment FAILED";
    }
}
