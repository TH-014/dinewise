package com.example.dinewise.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dinewise.dto.request.ExpenseRequestDTO;
import com.example.dinewise.dto.response.ExpenseResponseDTO;
import com.example.dinewise.model.Expense;
import com.example.dinewise.repo.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public ExpenseResponseDTO createExpense(ExpenseRequestDTO expenserequestDTO) {
        Expense expense = new Expense();
        expense.setManagerId(expenserequestDTO.getManagerId());
        expense.setDescription(expenserequestDTO.getDescription());
        expense.setPurchaseDate(expenserequestDTO.getPurchaseDate());
        expense.setTotalAmount(expenserequestDTO.getTotalAmount());

        // Save the expense to the database
        expense = expenseRepository.save(expense);

        // Create and return the response DTO
        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO(
        
            expense.getId(),
            expense.getManagerId(),
            expense.getDescription(),
            expense.getPurchaseDate(),
            expense.getTotalAmount()
        );
        
        return responseDTO;
    }


    public Optional<Expense> getPurchaseDate(LocalDate purchaseDate) {
        return expenseRepository.findByPurchaseDate(purchaseDate);
    }





    
}
