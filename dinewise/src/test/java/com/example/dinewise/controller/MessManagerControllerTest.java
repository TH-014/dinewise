package com.example.dinewise.controller;

import com.example.dinewise.config.TestSecurityConfig;
import com.example.dinewise.dto.request.ManagerApplicationRequestDTO;
import com.example.dinewise.dto.request.ManagerLoginRequestDTO;
import com.example.dinewise.model.ManagerStatus;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Student;
import com.example.dinewise.repo.MealConfirmationRepository;
import com.example.dinewise.service.MessManagerService;
import com.example.dinewise.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessManagerController.class)
@Import(TestSecurityConfig.class)
public class MessManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MessManagerService messManagerService;

    @MockBean
    private MealConfirmationRepository mealConfirmationRepository;

    @MockBean
    private com.example.dinewise.config.JwtGeneratorImpl jwtGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupSecurityContext() {
        Student mockStudent = new Student();
        mockStudent.setStdId("2005014");

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));

        var auth = new UsernamePasswordAuthenticationToken(mockStudent, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testLoginManager_Success() throws Exception {
        // Create student with matching ID and hashed password
        Student student = new Student();
        student.setStdId("2005014");
        student.setPasswordHash("hashedPass");

        // Create mess manager linked to the student
        MessManager manager = new MessManager();
        // manager.setStudent(student); // very important
        manager.setStdId(student.getStdId());
        manager.setStatus(ManagerStatus.running);
        manager.setStartDate(LocalDate.now());
        manager.setEndDate(LocalDate.now().plusMonths(1));
        manager.setAvgRating(4.5);

        // Mock service responses
        Mockito.when(studentService.getStudentByStudentId("2005014")).thenReturn(student);
        Mockito.when(passwordEncoder.matches("password", "hashedPass")).thenReturn(true);
        Mockito.when(messManagerService.getActiveManager("2005014")).thenReturn(manager);
        Mockito.when(jwtGenerator.generateToken("2005014", "manager"))
                .thenReturn(Map.of("token", "dummyToken"));

        // Construct login request
        ManagerLoginRequestDTO request = new ManagerLoginRequestDTO();
        request.setStdId("2005014");
        request.setPassword("password");

        // Perform and assert
        mockMvc.perform(post("/manager/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").value("dummyToken"))
                .andExpect(jsonPath("$.message").value("Manager login successful"));
    }

    @Test
    public void testLoginManager_InvalidCredentials() throws Exception {
        Mockito.when(studentService.getStudentByStudentId("2005014")).thenReturn(null);

        ManagerLoginRequestDTO request = new ManagerLoginRequestDTO();
        request.setStdId("2005014");
        request.setPassword("wrongpass");

        mockMvc.perform(post("/manager/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.text").value("Invalid credentials"));
    }

    @Test
    public void testLoginManager_NotAnActiveManager() throws Exception {
        Student student = new Student();
        student.setStdId("2005014");
        student.setPasswordHash("hashedPass");

        Mockito.when(studentService.getStudentByStudentId("2005014")).thenReturn(student);
        Mockito.when(passwordEncoder.matches("password", "hashedPass")).thenReturn(true);
        Mockito.when(messManagerService.getActiveManager("2005014")).thenReturn(null);

        ManagerLoginRequestDTO request = new ManagerLoginRequestDTO();
        request.setStdId("2005014");
        request.setPassword("password");

        mockMvc.perform(post("/manager/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.text").value("Not an active mess manager"));
    }

    // @Test
    // public void testApplyAsManager_Success() throws Exception {
    // Mockito.when(messManagerService.applyForManager(eq("2005014"),
    // eq(LocalDate.of(2025, 8, 1))))
    // .thenReturn(true);

    // ManagerApplicationRequestDTO request = new ManagerApplicationRequestDTO();
    // request.setStdId("2005014");
    // request.setAppliedMonth(LocalDate.of(2025, 8, 1));

    // mockMvc.perform(post("/manager/apply")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(request)))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.message").value("Application submitted
    // successfully"));
    // }

    // @Test
    // public void testApplyAsManager_AlreadyApplied() throws Exception {
    // Mockito.when(messManagerService.applyForManager(eq("2005014"),
    // eq(LocalDate.of(2025, 8, 1))))
    // .thenReturn(false);

    // ManagerApplicationRequestDTO request = new ManagerApplicationRequestDTO();
    // request.setStdId("2005014");
    // request.setAppliedMonth(LocalDate.of(2025, 8, 1));

    // mockMvc.perform(post("/manager/apply")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(request)))
    // .andExpect(status().isConflict())
    // .andExpect(jsonPath("$.message").value("Already applied for this month"));
    // }

    @Test
    public void testGetNextDayMealCounts() throws Exception {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Mockito.when(mealConfirmationRepository.countByMealDateAndWillLunchTrue(tomorrow)).thenReturn(10L);
        Mockito.when(mealConfirmationRepository.countByMealDateAndWillDinnerTrue(tomorrow)).thenReturn(15L);

        mockMvc.perform(get("/manager/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lunchCount").value(10))
                .andExpect(jsonPath("$.dinnerCount").value(15));
    }
}
