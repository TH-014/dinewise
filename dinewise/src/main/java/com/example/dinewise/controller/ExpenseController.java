package com.example.dinewise.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dinewise.dto.request.ExpenseRequestDTO;
import com.example.dinewise.dto.request.MealConfirmationRequestDTO;
import com.example.dinewise.dto.response.ExpenseResponseDTO;
import com.example.dinewise.dto.response.MealConfirmationResponseDTO;
import com.example.dinewise.dto.response.Message;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Student;
import com.example.dinewise.service.ExpenseService;


@RestController
@RequestMapping("/manager")
public class ExpenseController {

    // @Autowired
    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

     @PostMapping("/expense")
    public ResponseEntity<?> addExpense(@AuthenticationPrincipal MessManager manager, 
                                           @RequestBody ExpenseRequestDTO requestDTO) {

        String managerId = manager.getStdId();

         try {
            if (managerId == null || managerId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Message("Error: Manager ID is required."));
            }
            requestDTO.setManagerId(managerId);
            requestDTO.setPurchaseDate(LocalDate.now());

            ExpenseResponseDTO responseDTO = service.createExpense(requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Message("Error: " + e.getMessage()));
        }
                                            
    }


    
}

