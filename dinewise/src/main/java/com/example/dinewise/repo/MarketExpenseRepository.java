package com.example.dinewise.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.MarketExpense;

public interface MarketExpenseRepository extends JpaRepository<MarketExpense, Long> {}
