package com.example.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    private Integer customerId;
    private Integer restaurantId;

    private double totalPrice;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();
}