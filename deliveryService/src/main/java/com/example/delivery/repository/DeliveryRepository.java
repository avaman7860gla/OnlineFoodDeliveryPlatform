package com.example.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.delivery.entity.DeliveryAgent;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryAgent, Integer> {

    Optional<DeliveryAgent> findByUserId(Integer userId);

    List<DeliveryAgent> findByIsAvailableTrue();

    List<DeliveryAgent> findByIsVerifiedTrue();

    List<DeliveryAgent> findByIsAvailableTrueAndIsVerifiedTrue();
}