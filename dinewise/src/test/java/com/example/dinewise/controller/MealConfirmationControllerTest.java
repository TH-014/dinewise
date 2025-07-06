package com.example.dinewise.controller;

import com.example.dinewise.config.TestSecurityConfig;
import com.example.dinewise.dto.request.MealConfirmationRequestDTO;
import com.example.dinewise.model.Student;
import com.example.dinewise.service.MealConfirmationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(MealConfirmationController.class)
// @SpringBootTest
// @AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
// @WebMvcTest
public class MealConfirmationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealConfirmationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupSecurityContext() {
        Student mockStudent = new Student();
        mockStudent.setStdId("2005011");

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));

        var auth = new UsernamePasswordAuthenticationToken(mockStudent, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testConfirmMeal_Success() throws Exception {
        MealConfirmationRequestDTO requestDTO = new MealConfirmationRequestDTO();
        requestDTO.setMealDate(LocalDate.now().plusDays(1));
        requestDTO.setWillLunch(true);
        requestDTO.setWillDinner(false);

        mockMvc.perform(post("/student/mealconfirmation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(service).confirmMeal(eq("2005011"), any());
    }
}
