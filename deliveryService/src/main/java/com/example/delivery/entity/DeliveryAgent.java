package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer agentId;

    private Integer userId;

    private String fullName;
    private String phone;

    private String vehicleType;
    private String vehicleNumber;

    private Double currentLatitude;
    private Double currentLongitude;

    private Boolean isAvailable = false;
    private Boolean isVerified = false;

    private Double avgRating = 0.0;
    private Integer totalDeliveries = 0;
}
