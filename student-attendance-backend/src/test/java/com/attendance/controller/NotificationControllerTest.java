package com.attendance.controller;

import com.attendance.model.Notification;
import com.attendance.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());
        when(notificationService.findAll()).thenReturn(notifications);

        List<Notification> result = notificationController.getAllNotifications();
        assertEquals(1, result.size());
    }

    @Test
    void testGetNotificationById() {
        Notification notification = new Notification();
        notification.setId(1L);
        when(notificationService.findById(1L)).thenReturn(Optional.of(notification));

        ResponseEntity<Notification> response = notificationController.getNotificationById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCreateNotification() {
        Notification notification = new Notification();
        when(notificationService.save(any(Notification.class))).thenReturn(notification);

        ResponseEntity<Notification> response = notificationController.createNotification(notification);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testDeleteNotification() {
        ResponseEntity<Void> response = notificationController.deleteNotification(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).deleteById(1L);
    }
}
