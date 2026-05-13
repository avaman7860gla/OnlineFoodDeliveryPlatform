package com.example.notification.service;

import com.example.notification.entity.Notification;
import com.example.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;

    @Override
    public void send(Notification notification) {
        notification.setIsRead(false);
        notification.setSentAt(java.time.LocalDateTime.now());

        repo.save(notification);

        // MOCK CHANNELS
        if ("EMAIL".equalsIgnoreCase(notification.getChannel())) {
            sendEmail("test@mail.com", notification.getTitle(), notification.getMessage());
        }

        if ("SMS".equalsIgnoreCase(notification.getChannel())) {
            sendSMS("9999999999", notification.getMessage());
        }
    }

    @Override
    public void sendBulk(List<Integer> recipients, String title, String message) {
        for (Integer id : recipients) {
            Notification n = new Notification();
            n.setRecipientId(id);
            n.setTitle(title);
            n.setMessage(message);
            n.setType("PROMO");
            n.setChannel("APP");
            send(n);
        }
    }

    @Override
    public void markAsRead(Integer id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        n.setIsRead(true);
        repo.save(n);
    }

    @Override
    public void markAllRead(Integer recipientId) {
        List<Notification> list = repo.findByRecipientId(recipientId);
        list.forEach(n -> n.setIsRead(true));
        repo.saveAll(list);
    }

    @Override
    public List<Notification> getByRecipient(Integer recipientId) {
        return repo.findByRecipientId(recipientId);
    }

    @Override
    public int getUnreadCount(Integer recipientId) {
        return repo.countByRecipientIdAndIsRead(recipientId, false);
    }

    @Override
    public void deleteNotification(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<Notification> getAll() {
        return repo.findAll();
    }

    @Override
    public void sendEmail(String email, String subject, String message) {
        System.out.println("📧 EMAIL SENT → " + subject);
    }

    @Override
    public void sendSMS(String phone, String message) {
        System.out.println("📱 SMS SENT → " + message);
    }
}