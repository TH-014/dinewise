package com.example.dinewise.service;

import com.example.dinewise.model.Stock;
import com.example.dinewise.repo.StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock addStock(Stock stock) {
        stock.setLastUpdated(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock updated) {
        Stock existing = stockRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stock item not found"));
        existing.setItemName(updated.getItemName());
        existing.setUnit(updated.getUnit());
        existing.setQuantity(updated.getQuantity());
        existing.setPerUnitPrice(updated.getPerUnitPrice());
        existing.setLastUpdated(LocalDateTime.now());
        return stockRepository.save(existing);
    }

}

