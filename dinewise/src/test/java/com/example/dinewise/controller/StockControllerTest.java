package com.example.dinewise.controller;

import com.example.dinewise.config.TestSecurityConfig;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Stock;
import com.example.dinewise.service.StockServiceImpl;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
@Import(TestSecurityConfig.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockServiceImpl stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupSecurityContext() {
        MessManager mockManager = new MessManager();
        mockManager.setStdId("2005001");

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));

        var auth = new UsernamePasswordAuthenticationToken(mockManager, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testGetStocks() throws Exception {
        Stock stock1 = new Stock(1L, "Rice", "kg", 20.0, 40.0, LocalDateTime.now());
        Stock stock2 = new Stock(2L, "Oil", "L", 10.0, 150.0, LocalDateTime.now());

        Mockito.when(stockService.getAllStocks()).thenReturn(List.of(stock1, stock2));

        mockMvc.perform(get("/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void testAddStock() throws Exception {
        Stock stock = new Stock(null, "Dal", "kg", 25.0, 50.0, null);
        Stock savedStock = new Stock(3L, "Dal", "kg", 25.0, 50.0, LocalDateTime.now());

        Mockito.when(stockService.addStock(any(Stock.class))).thenReturn(savedStock);

        mockMvc.perform(post("/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Dal"));
    }

    @Test
    public void testUpdateStock() throws Exception {
        Stock updatedStock = new Stock(1L, "Rice", "kg", 30.0, 45.0, LocalDateTime.now());

        Mockito.when(stockService.updateStock(Mockito.eq(1L), any(Stock.class))).thenReturn(updatedStock);

        mockMvc.perform(put("/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(30.0));
    }
}
