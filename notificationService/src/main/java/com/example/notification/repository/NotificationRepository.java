package com.example.notification.repository;

import com.example.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByRecipientId(Integer recipientId);

    List<Notification> findByRecipientIdAndIsRead(Integer recipientId, Boolean isRead);

    int countByRecipientIdAndIsRead(Integer recipientId, Boolean isRead);

    List<Notification> findByType(String type);

    List<Notification> findByRelatedId(Integer relatedId);

    void deleteByNotificationId(Integer id);
}