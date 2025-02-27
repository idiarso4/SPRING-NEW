package com.attendance.service;

import com.attendance.model.Student;
import com.attendance.repository.StudentRepository;
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

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSave() {
        Student student = new Student();
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.save(student);
        assertNotNull(result);
    }

    @Test
    void testDeleteById() {
        studentService.deleteById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
