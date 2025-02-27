package com.attendance.controller;

import com.attendance.model.Student;
import com.attendance.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        when(studentService.findAll()).thenReturn(students);

        List<Student> result = studentController.getAllStudents();
        assertEquals(1, result.size());
    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        ResponseEntity<Student> response = studentController.getStudentById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        when(studentService.save(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = studentController.createStudent(student);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.save(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = studentController.updateStudent(1L, student);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteStudent() {
        ResponseEntity<Void> response = studentController.deleteStudent(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService, times(1)).deleteById(1L);
    }
}
