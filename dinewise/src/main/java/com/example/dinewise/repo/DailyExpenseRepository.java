package com.example.dinewise.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.DailyExpense;

public interface DailyExpenseRepository extends JpaRepository<DailyExpense, Long> {}