package com.example.dinewise.controller;

import com.example.dinewise.model.Stock;
import com.example.dinewise.service.StockService;
import com.example.dinewise.service.StockServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {
    @Autowired
    private StockServiceImpl stockService;

    // public StockController(StockService stockService) {
    //     this.stockService = stockService;
    // }

    @GetMapping
    public ResponseEntity<List<Stock>> getStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

     @PostMapping
    public ResponseEntity<?> addStock(@RequestBody Stock stock) {
        stock.setLastUpdated(LocalDateTime.now());
        return ResponseEntity.ok(stockService.addStock(stock));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
        return ResponseEntity.ok(stockService.updateStock(id, updatedStock));
    }
}

