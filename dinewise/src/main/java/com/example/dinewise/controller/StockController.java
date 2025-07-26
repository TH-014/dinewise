// package com.example.dinewise.controller;

// import com.example.dinewise.model.Stock;
// import com.example.dinewise.service.StockService;
// import com.example.dinewise.service.StockServiceImpl;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDateTime;
// import java.util.List;

// @RestController
// @RequestMapping("/stocks")
// public class StockController {
//     @Autowired
//     private StockServiceImpl stockService;

//     // public StockController(StockService stockService) {
//     //     this.stockService = stockService;
//     // }

//     @GetMapping
//     public ResponseEntity<List<Stock>> getStocks() {
//         return ResponseEntity.ok(stockService.getAllStocks());
//     }

//      @PostMapping
//     public ResponseEntity<?> addStock(@RequestBody Stock stock) {
//         stock.setLastUpdated(LocalDateTime.now());
//         return ResponseEntity.ok(stockService.addStock(stock));
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
//         return ResponseEntity.ok(stockService.updateStock(id, updatedStock));
//     }
// }



package com.example.dinewise.controller;

import com.example.dinewise.model.DailyExpense;
import com.example.dinewise.model.MarketExpense;
import com.example.dinewise.model.MealType;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Stock;
import com.example.dinewise.repo.DailyExpenseRepository;
import com.example.dinewise.repo.MarketExpenseRepository;
import com.example.dinewise.service.StockService;
import com.example.dinewise.service.StockServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired private MarketExpenseRepository marketRepo;
    @Autowired private DailyExpenseRepository dailyRepo;

    @PostMapping("/{id}/add")
    public ResponseEntity<?> addToStock(@PathVariable Long id, @RequestParam double quantity, @RequestParam double price, @AuthenticationPrincipal MessManager manager) {
        Stock stock = stockService.getStockById(id);
        stock.setQuantity(stock.getQuantity() + quantity);
        if(price != -1) {
            stock.setPerUnitPrice(price);
        }
        stock.setLastUpdated(LocalDateTime.now());
        stockService.addStock(stock);

        MarketExpense expense = new MarketExpense();
        expense.setStock(stock);
        expense.setQuantityAdded(quantity);
        expense.setTotalCost(quantity * stock.getPerUnitPrice());
        expense.setAddedBy(manager.getStdId());
        marketRepo.save(expense);

        return ResponseEntity.ok(stock);
    }

    @PostMapping("/{id}/subtract")
    public ResponseEntity<?> subtractFromStock(@PathVariable Long id, @RequestParam double quantity,
                                            @RequestParam MealType usedFor,
                                            @AuthenticationPrincipal MessManager manager) {
        Stock stock = stockService.getStockById(id);
        if (stock.getQuantity() < quantity) {
            return ResponseEntity.badRequest().body("Insufficient stock");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        stock.setLastUpdated(LocalDateTime.now());
        stockService.addStock(stock);

        DailyExpense expense = new DailyExpense();
        expense.setStock(stock);
        expense.setQuantityUsed(quantity);
        expense.setUsedBy(manager.getStdId());
        expense.setUsedFor(usedFor);
        dailyRepo.save(expense);

        return ResponseEntity.ok(stock);
    }


    @GetMapping
    public ResponseEntity<List<Stock>> getStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @PostMapping
    public ResponseEntity<?> addStock(@RequestBody Stock stock, @AuthenticationPrincipal MessManager manager) {
        stock.setLastUpdated(LocalDateTime.now());
        Stock savedStock = stockService.addStock(stock);

        MarketExpense expense = new MarketExpense();
         expense.setStock(savedStock); // now it has an ID
        expense.setQuantityAdded(savedStock.getQuantity());
        expense.setTotalCost(savedStock.getQuantity() * savedStock.getPerUnitPrice());
        expense.setAddedBy(manager.getStdId());
        marketRepo.save(expense);

        // return ResponseEntity.ok(stock);
        return ResponseEntity.ok(savedStock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
        return ResponseEntity.ok(stockService.updateStock(id, updatedStock));
    }
}

