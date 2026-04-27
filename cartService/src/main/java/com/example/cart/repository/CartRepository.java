package com.example.cart.repository;

import com.example.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByCustomerId(Integer customerId);
    
    Optional<Cart> findByCartId(Integer cartId);

    boolean existsByCustomerId(Integer customerId);

    List<Cart> findByRestaurantId(Integer restaurantId);

    void deleteByCustomerId(Integer customerId);
}