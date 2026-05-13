package com.example.menu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    private Integer restaurantId;
    private Integer categoryId;

    private String name;
    private String description;

    private double price;
    private double discountedPrice;

    private String imageUrl;

    @com.fasterxml.jackson.annotation.JsonProperty("isVeg")
    private boolean isVeg;
    
    @com.fasterxml.jackson.annotation.JsonProperty("isAvailable")
    private boolean isAvailable = true;

    private double rating;

    private int calories;

    private String tags;
}