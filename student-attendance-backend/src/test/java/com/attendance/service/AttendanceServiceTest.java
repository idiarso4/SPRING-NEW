package com.attendance.service;

import com.attendance.model.Attendance;
import com.attendance.repository.AttendanceRepository;
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

class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Attendance> attendanceList = new ArrayList<>();
        attendanceList.add(new Attendance());
        when(attendanceRepository.findAll()).thenReturn(attendanceList);

        List<Attendance> result = attendanceService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Attendance attendance = new Attendance();
        attendance.setId(1L);
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));

        Optional<Attendance> result = attendanceService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSave() {
        Attendance attendance = new Attendance();
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = attendanceService.save(attendance);
        assertNotNull(result);
    }

    @Test
    void testDeleteById() {
        attendanceService.deleteById(1L);
        verify(attendanceRepository, times(1)).deleteById(1L);
    }
}
