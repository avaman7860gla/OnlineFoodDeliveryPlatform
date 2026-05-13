package com.example.menu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private Integer restaurantId;

    private String name;
    private String description;

    private String imageUrl;

    private int displayOrder;
}