package com.example.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    private Integer recipientId;

    private String type; // ORDER, PAYMENT, DELIVERY, PROMO

    private String title;
    private String message;

    private String channel; // APP, EMAIL, SMS

    private Integer relatedId;
    private String relatedType;

    private Boolean isRead = false;

    private LocalDateTime sentAt = LocalDateTime.now();
}