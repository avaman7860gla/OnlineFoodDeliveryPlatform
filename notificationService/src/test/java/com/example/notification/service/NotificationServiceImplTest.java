package com.example.notification.service;

import com.example.notification.entity.Notification;
import com.example.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationServiceImpl service;

    private Notification notif;

    @BeforeEach
    void setUp() {
        notif = new Notification();
        notif.setNotificationId(1);
        notif.setRecipientId(10);
        notif.setTitle("Test");
        notif.setMessage("Message");
        notif.setIsRead(false);
    }

    @Test
    void testSendNotification() {
        when(repository.save(any(Notification.class))).thenReturn(notif);
        service.send(notif);
        verify(repository, times(1)).save(notif);
        assertFalse(notif.getIsRead());
        assertNotNull(notif.getSentAt());
    }

    @Test
    void testMarkAsRead() {
        when(repository.findById(1)).thenReturn(java.util.Optional.of(notif));
        service.markAsRead(1);
        assertTrue(notif.getIsRead());
        verify(repository, times(1)).save(notif);
    }
}
