package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;

    private Integer ownerId;
    private String ownerEmail;  // Used to look up restaurant by JWT token email

    private String name;
    private String description;
    private String cuisine;

    private String address;
    private String city;

    private double latitude;
    private double longitude;

    private String phone;

    private double avgRating = 0.0;

    private boolean isOpen = true;
    private boolean isApproved = false;

    private double deliveryRadius;
    private double minOrderAmount;

    private int estimatedDeliveryMin;
}