package com.example.dinewise.controller;

import com.example.dinewise.config.TestSecurityConfig;
import com.example.dinewise.dto.request.LoginRequestDTO;
import com.example.dinewise.dto.request.SignupRequestDTO;
import com.example.dinewise.dto.response.Message;
import com.example.dinewise.model.Student;
import com.example.dinewise.model.UserRequest;
import com.example.dinewise.repo.UserRequestRepository;
import com.example.dinewise.service.EmailSenderService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(TestSecurityConfig.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRequestRepository userRequestRepository;

    @MockBean
    private EmailSenderService emailSenderService;

    @MockBean
    private com.example.dinewise.config.JwtGeneratorInterface jwtGenerator;

    @Test
    public void testLoginStudent_Success() throws Exception {
        Student student = new Student();
        student.setStdId("2005001");
        student.setPasswordHash("hashedPassword");

        Mockito.when(studentService.getStudentByStudentId("2005001")).thenReturn(student);
        Mockito.when(passwordEncoder.matches("correctPass", "hashedPassword")).thenReturn(true);
        Mockito.when(jwtGenerator.generateToken(student)).thenReturn(Map.of(
                "token", "dummyToken",
                "message", "Login successful"));

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setStudentId("2005001");
        loginRequest.setPassword("correctPass");
        loginRequest.setUserName("dummy");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").value("dummyToken"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    public void testLoginStudent_InvalidCredentials() throws Exception {
        Mockito.when(studentService.getStudentByStudentId("2005001")).thenReturn(null);

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setStudentId("2005001");
        loginRequest.setPassword("wrongPass");
        loginRequest.setUserName("dummy");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.text").value("UserName or Password is invalid"));
    }

    @Test
    public void testSignupRequest_NewUser() throws Exception {
        SignupRequestDTO signupRequest = new SignupRequestDTO();
        signupRequest.setEmail("test@example.com");
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("password123");
        signupRequest.setStdId("2005001");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPhoneNumber("01234567890");
        signupRequest.setImageUrl("http://example.com/image.jpg");
        signupRequest.setPresentAddress("123 Street");
        signupRequest.setPermanentAddress("123 Street");

        Mockito.when(userRequestRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/signup/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("OTP sent to your email"));
    }

    @Test
    public void testVerifyOtp_Success() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setOtp("123456");
        userRequest.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRequest.setVerified(false);

        Mockito.when(userRequestRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userRequest));

        Map<String, String> payload = Map.of(
                "email", "test@example.com",
                "otp", "123456");

        mockMvc.perform(post("/signup/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Signup complete. You may now log in."));
    }

    @Test
    public void testSignupUser_EmailAlreadyTaken() throws Exception {
        SignupRequestDTO signupRequest = new SignupRequestDTO();
        signupRequest.setEmail("test@example.com");
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("password123");
        signupRequest.setStdId("2005001");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPhoneNumber("01234567890");
        signupRequest.setImageUrl("http://example.com/image.jpg");
        signupRequest.setPresentAddress("123 Street");
        signupRequest.setPermanentAddress("123 Street");

        Student existingStudent = new Student();
        Mockito.when(studentService.getStudentByStudentId("2005001")).thenReturn(existingStudent);

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.text").value("Student Id is already taken"));
    }
}
