package com.example.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "razorpay")
@Data
public class RazorpayConfig {
    private String key;
    private String secret;
}