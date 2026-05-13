package com.example.notification.controller;

import com.example.notification.entity.Notification;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/send")
    public String send(@RequestBody Notification notification) {
        service.send(notification);
        return "Notification Sent";
    }

    @PostMapping("/bulk")
    public String sendBulk(@RequestBody List<Integer> users,
                           @RequestParam String title,
                           @RequestParam String message) {
        service.sendBulk(users, title, message);
        return "Bulk Sent";
    }

    @GetMapping("/{userId}")
    public List<Notification> getByRecipient(@PathVariable Integer userId) {
        return service.getByRecipient(userId);
    }

    @PutMapping("/read/{id}")
    public String markRead(@PathVariable Integer id) {
        service.markAsRead(id);
        return "Marked Read";
    }

    @PutMapping("/read/all/{userId}")
    public String markAll(@PathVariable Integer userId) {
        service.markAllRead(userId);
        return "All Read";
    }

    @GetMapping("/unread/{userId}")
    public int unread(@PathVariable Integer userId) {
        return service.getUnreadCount(userId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteNotification(id);
        return "Deleted";
    }

    @GetMapping("/all")
    public List<Notification> getAll() {
        return service.getAll();
    }
}