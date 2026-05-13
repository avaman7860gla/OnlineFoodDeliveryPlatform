package com.example.payment.service;

import com.example.payment.config.RazorpayConfig;
import com.example.payment.entity.Payment;
import com.example.payment.entity.Wallet;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private WalletRepository walletRepo;

    @Mock
    private RazorpayConfig config;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentServiceImpl service;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setWalletId(1);
        wallet.setCustomerId(10);
        wallet.setBalance(100.0);
    }

    @Test
    void testGetWalletBalance_Exists() {
        when(walletRepo.findByCustomerId(10)).thenReturn(Optional.of(wallet));
        double balance = service.getWalletBalance(10);
        assertEquals(100.0, balance);
    }

    @Test
    void testGetWalletBalance_NewWallet() {
        when(walletRepo.findByCustomerId(99)).thenReturn(Optional.empty());
        double balance = service.getWalletBalance(99);
        assertEquals(0.0, balance);
    }

    @Test
    void testAddToWallet() {
        when(walletRepo.findByCustomerId(10)).thenReturn(Optional.of(wallet));
        when(walletRepo.save(any(Wallet.class))).thenReturn(wallet);

        service.addToWallet(10, 50.0);
        
        assertEquals(150.0, wallet.getBalance());
        verify(walletRepo, times(1)).save(wallet);
    }

    @Test
    void testPayFromWallet_Success() {
        when(walletRepo.findByCustomerId(10)).thenReturn(Optional.of(wallet));
        when(walletRepo.save(any(Wallet.class))).thenReturn(wallet);
        when(paymentRepo.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = service.payFromWallet(10, 40.0);
        
        assertNotNull(payment);
        assertEquals("WALLET", payment.getMode());
        assertEquals("PAID", payment.getStatus());
        assertEquals(60.0, wallet.getBalance());
        verify(walletRepo, times(1)).save(wallet);
    }

    @Test
    void testPayFromWallet_InsufficientBalance() {
        when(walletRepo.findByCustomerId(10)).thenReturn(Optional.of(wallet));

        assertThrows(RuntimeException.class, () -> {
            service.payFromWallet(10, 150.0);
        });
        
        assertEquals(100.0, wallet.getBalance()); // Balance unchanged
        verify(walletRepo, never()).save(any(Wallet.class));
    }
}
