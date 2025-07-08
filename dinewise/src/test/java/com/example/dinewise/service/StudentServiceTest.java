package com.example.dinewise.service;

import com.example.dinewise.model.Student;
import com.example.dinewise.repo.StudentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    private StudentRepo studentRepo;
    private StudentService studentService;

    @BeforeEach
    public void setup() throws Exception {
        studentRepo = mock(StudentRepo.class);
        studentService = new StudentService();

        // Inject mock into private field via reflection
        Field repoField = StudentService.class.getDeclaredField("studentRepo");
        repoField.setAccessible(true);
        repoField.set(studentService, studentRepo);
    }

    @Test
    public void testGetStudentByUsername_ReturnsStudent() {
        Student student = new Student();
        student.setUsername("tanvir");

        when(studentRepo.findByUsername("tanvir")).thenReturn(student);

        Student result = studentService.getStudentByUsername("tanvir");

        assertNotNull(result);
        assertEquals("tanvir", result.getUsername());
    }

    @Test
    public void testGetStudentByName_ReturnsStudent() {
        Student student = new Student();
        student.setFirstName("Tanvir");

        when(studentRepo.findByFirstName("Tanvir")).thenReturn(student);

        Student result = studentService.getStudentByName("Tanvir");

        assertNotNull(result);
        assertEquals("Tanvir", result.getFirstName());
    }

    @Test
    public void testGetStudentByStudentId_ReturnsStudent() {
        Student student = new Student();
        student.setStdId("2005001");

        when(studentRepo.findByStdId("2005001")).thenReturn(student);

        Student result = studentService.getStudentByStudentId("2005001");

        assertNotNull(result);
        assertEquals("2005001", result.getStdId());
    }

    @Test
    public void testGetStudentByUsernameAndPasswordHash_ReturnsStudent() {
        Student student = new Student();
        student.setUsername("tanvir");
        student.setPasswordHash("hashed123");

        when(studentRepo.findByUsernameAndPasswordHash("tanvir", "hashed123")).thenReturn(student);

        Student result = studentService.getStudentByUserNameAndPasswordHash("tanvir", "hashed123");

        assertNotNull(result);
        assertEquals("tanvir", result.getUsername());
    }

    @Test
    public void testGetStudentByEmail_ReturnsStudent() {
        Student student = new Student();
        student.setEmail("tanvir@example.com");

        when(studentRepo.findByEmail("tanvir@example.com")).thenReturn(student);

        Student result = studentService.getStudentByEmail("tanvir@example.com");

        assertNotNull(result);
        assertEquals("tanvir@example.com", result.getEmail());
    }

    @Test
    public void testSaveStudent_ReturnsSavedStudent() {
        Student student = new Student();
        student.setUsername("tanvir");

        when(studentRepo.save(student)).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertNotNull(result);
        assertEquals("tanvir", result.getUsername());
    }
}
