package com.example.notification.service;

import com.example.notification.entity.Notification;

import java.util.List;

public interface NotificationService {

    void send(Notification notification);

    void sendBulk(List<Integer> recipients, String title, String message);

    void markAsRead(Integer notificationId);

    void markAllRead(Integer recipientId);

    List<Notification> getByRecipient(Integer recipientId);

    int getUnreadCount(Integer recipientId);

    void deleteNotification(Integer id);

    List<Notification> getAll();

    void sendEmail(String email, String subject, String message);

    void sendSMS(String phone, String message);
}