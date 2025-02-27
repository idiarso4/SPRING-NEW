package com.attendance.service;

import com.attendance.model.Notification;
import com.attendance.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Notification notification = new Notification();
        notification.setId(1L);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSave() {
        Notification notification = new Notification();
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.save(notification);
        assertNotNull(result);
    }

    @Test
    void testDeleteById() {
        notificationService.deleteById(1L);
        verify(notificationRepository, times(1)).deleteById(1L);
    }
}
