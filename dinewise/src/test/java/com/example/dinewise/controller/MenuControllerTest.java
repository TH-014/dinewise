package com.example.dinewise.controller;

import com.example.dinewise.config.TestSecurityConfig;
import com.example.dinewise.model.Menu;
import com.example.dinewise.repo.MenuRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
@Import(TestSecurityConfig.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuRepository menuRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetMenuByDate_Found() throws Exception {
        LocalDate date = LocalDate.now();
        Menu menu = new Menu();
        menu.setMenuDate(date);
        menu.setLunchItems(List.of("Rice", "Fish"));
        menu.setDinnerItems(List.of("Bread", "Chicken"));

        when(menuRepo.findByMenuDate(date)).thenReturn(Optional.of(menu));

        mockMvc.perform(get("/menus")
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.menuDate").value(date.toString()))
                .andExpect(jsonPath("$.lunchItems[0]").value("Rice"))
                .andExpect(jsonPath("$.dinnerItems[1]").value("Chicken"));
    }

    @Test
    public void testGetMenuByDate_NotFound() throws Exception {
        LocalDate date = LocalDate.now();
        when(menuRepo.findByMenuDate(date)).thenReturn(Optional.empty());

        mockMvc.perform(get("/menus")
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No menu found for selected date"));
    }

    @Test
    public void testGetMenuByDate_InvalidFormat() throws Exception {
        mockMvc.perform(get("/menus")
                .param("date", "invalid-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid date format or internal error"));
    }

    @Test
    public void testCreateOrUpdateMenu_Success() throws Exception {
        LocalDate date = LocalDate.now().plusDays(1);
        Map<String, Object> request = Map.of(
                "menuDate", date.toString(),
                "lunchItems", List.of("Rice", "Fish"),
                "dinnerItems", List.of("Bread", "Chicken"));

        when(menuRepo.findByMenuDate(date)).thenReturn(Optional.empty());

        mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Menu saved successfully"));

        verify(menuRepo, times(1)).save(any(Menu.class));
    }

    @Test
    public void testCreateOrUpdateMenu_PastDate() throws Exception {
        LocalDate date = LocalDate.now().minusDays(1);
        Map<String, Object> request = Map.of(
                "menuDate", date.toString(),
                "lunchItems", List.of("Rice"),
                "dinnerItems", List.of("Bread"));

        mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cannot set menu for past date"));

        verify(menuRepo, never()).save(any());
    }
}
