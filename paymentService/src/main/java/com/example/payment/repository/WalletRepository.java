package com.example.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payment.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findByCustomerId(Integer customerId);
}