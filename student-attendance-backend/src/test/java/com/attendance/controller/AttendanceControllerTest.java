package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.service.AttendanceService;
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

class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceController attendanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        attendanceList.add(new Attendance());
        when(attendanceService.findAll()).thenReturn(attendanceList);

        List<Attendance> result = attendanceController.getAllAttendance();
        assertEquals(1, result.size());
    }

    @Test
    void testGetAttendanceById() {
        Attendance attendance = new Attendance();
        attendance.setId(1L);
        when(attendanceService.findById(1L)).thenReturn(Optional.of(attendance));

        ResponseEntity<Attendance> response = attendanceController.getAttendanceById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCreateAttendance() {
        Attendance attendance = new Attendance();
        when(attendanceService.save(any(Attendance.class))).thenReturn(attendance);

        ResponseEntity<Attendance> response = attendanceController.createAttendance(attendance);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testDeleteAttendance() {
        ResponseEntity<Void> response = attendanceController.deleteAttendance(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(attendanceService, times(1)).deleteById(1L);
    }
}
