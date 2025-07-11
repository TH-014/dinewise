package com.example.dinewise.controller;
import com.example.dinewise.dto.request.MealConfirmationRequestDTO;
import com.example.dinewise.dto.response.MealConfirmationResponseDTO;
import com.example.dinewise.dto.response.Message;
import com.example.dinewise.model.Due;
import com.example.dinewise.model.MealConfirmation;
import com.example.dinewise.model.Student;
import com.example.dinewise.service.MealConfirmationService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/student")
public class MealConfirmationController {

    private final MealConfirmationService service;

    public MealConfirmationController(MealConfirmationService service) {
        this.service = service;
    }

    @PostMapping("/mealconfirmation")
    public ResponseEntity<?> confirmMeal(@AuthenticationPrincipal Student student, 
                                           @RequestBody MealConfirmationRequestDTO requestDTO) {

        String stdId = student.getStdId();
        System.out.println(stdId);
        try {
            if (stdId == null || stdId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Message("Error: Student ID is required."));
            }

            MealConfirmationResponseDTO responseDTO = service.confirmMeal(stdId, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Message("Error: " + e.getMessage()));
        }
    }



    @GetMapping("/from/{date}")
    public ResponseEntity<?> getMyMealConfirmationsFromDate(
            @AuthenticationPrincipal Student authenticatedStudent,
            @PathVariable String date) {
            System.out.println(authenticatedStudent);
        try {
            if (authenticatedStudent == null || authenticatedStudent.getStdId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new Message("Unauthorized: Please log in."));
            }

            LocalDate startDate = LocalDate.parse(date);
            List<MealConfirmation> confirmations =
                    service.getMealConfirmationsFromDate(authenticatedStudent.getStdId(), startDate);

            return ResponseEntity.ok(confirmations);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Message("Invalid date format. Use YYYY-MM-DD"));
        }
    }

    @DeleteMapping("/mealconfirmation/{id}")
    public ResponseEntity<?> deleteMealConfirmation(
            @PathVariable Long id,
            @AuthenticationPrincipal Student student) {

        Optional<MealConfirmation> meal = service.getMealConfirmationByMealId(id);

        if (meal.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Meal not found"));
        }

        MealConfirmation confirmation = meal.get();

        // Optional: restrict deletion to only the owner (student)
        if (!confirmation.getStdId().equals(student.getStdId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Message("Unauthorized to delete this meal"));
        }

        // Optional: restrict to future meals only
        if (confirmation.getMealDate().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Cannot delete past meals"));
        }

        service.deleteMealConfirmation(id);
        return ResponseEntity.ok(new Message("Meal confirmation deleted"));
    }

    @GetMapping("/dues")
    public ResponseEntity<?> getMyDues(@AuthenticationPrincipal Student student) {
        Due due = service.getDueByStudentId(student.getStdId());
        if (due == null) {
            return ResponseEntity.ok(Map.of("totalDue", 0.0));
        }
        return ResponseEntity.ok(Map.of("totalDue", due.getTotalDue()));
    }

    @GetMapping("/meals/since-last-payment/{stdId}")
    public ResponseEntity<List<MealConfirmationResponseDTO>> getMealsSinceLastPayment(@PathVariable String stdId) {
        List<MealConfirmation> meals = service.getMealsAfterLastPayment(stdId);
        List<MealConfirmationResponseDTO> dtoList = meals.stream()
            .map(MealConfirmationResponseDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


    

}
