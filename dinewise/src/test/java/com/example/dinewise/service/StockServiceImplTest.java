package com.example.dinewise.service;

import com.example.dinewise.model.Stock;
import com.example.dinewise.repo.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceImplTest {

    private StockRepository stockRepository;
    private StockServiceImpl stockService;

    @BeforeEach
    public void setup() {
        stockRepository = Mockito.mock(StockRepository.class);
        stockService = new StockServiceImpl(stockRepository);
    }

    @Test
    public void testGetAllStocks_ReturnsList() {
        List<Stock> mockList = Arrays.asList(
                new Stock(1L, "Rice", "kg", 20.0, 50.0, LocalDateTime.now()),
                new Stock(2L, "Oil", "litre", 10.0, 150.0, LocalDateTime.now())
        );

        when(stockRepository.findAll()).thenReturn(mockList);

        List<Stock> result = stockService.getAllStocks();

        assertEquals(2, result.size());
        assertEquals("Rice", result.get(0).getItemName());
    }

    @Test
    public void testAddStock_SetsLastUpdatedAndSaves() {
        Stock input = new Stock();
        input.setItemName("Salt");
        input.setUnit("kg");
        input.setQuantity(5.0);
        input.setPerUnitPrice(10.0);

        Stock saved = new Stock(1L, "Salt", "kg", 5.0, 10.0, LocalDateTime.now());

        when(stockRepository.save(any(Stock.class))).thenReturn(saved);

        Stock result = stockService.addStock(input);

        assertNotNull(result);
        assertEquals("Salt", result.getItemName());

        ArgumentCaptor<Stock> captor = ArgumentCaptor.forClass(Stock.class);
        verify(stockRepository).save(captor.capture());

        Stock stockToSave = captor.getValue();
        assertNotNull(stockToSave.getLastUpdated());
    }

    @Test
    public void testUpdateStock_UpdatesFieldsAndSaves() {
        Stock existing = new Stock(1L, "Flour", "kg", 10.0, 30.0, LocalDateTime.now().minusDays(1));
        Stock updated = new Stock(null, "Flour", "kg", 15.0, 35.0, null);

        when(stockRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(stockRepository.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

        Stock result = stockService.updateStock(1L, updated);

        assertEquals(15.0, result.getQuantity());
        assertEquals(35.0, result.getPerUnitPrice());
        assertNotNull(result.getLastUpdated());

        verify(stockRepository).save(existing);
    }

    @Test
    public void testUpdateStock_ThrowsIfNotFound() {
        when(stockRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            stockService.updateStock(99L, new Stock());
        });

        assertEquals("Stock item not found", ex.getMessage());
    }
}
