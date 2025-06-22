package com.example.dinewise.repo;

import com.example.dinewise.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Additional query methods can be defined here if needed
    // For example, to find stocks by a specific attribute:
    // List<Stock> findByAttribute(String attributeName);
    // You can also define custom queries using @Query annotation if necessary
    // Example: @Query("SELECT s FROM Stock s WHERE s.attribute = ?1")
    // List<Stock> findByAttribute(String attributeName);
}

