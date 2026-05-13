package com.example.notification.service;

import com.example.notification.entity.Notification;
import com.example.notification.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeMessageFromQueue(Notification notification) {
        System.out.println("Message received from RabbitMQ: " + notification);
        try {
            notificationService.send(notification);
            System.out.println("Notification processed successfully via RabbitMQ");
        } catch (Exception e) {
            System.err.println("Error processing notification from RabbitMQ: " + e.getMessage());
        }
    }
}
