package com.example.dinewise.controller;
import com.example.dinewise.dto.request.MealConfirmationRequestDTO;
import com.example.dinewise.dto.response.MealConfirmationResponseDTO;
import com.example.dinewise.dto.response.Message;
import com.example.dinewise.model.MealConfirmation;
import com.example.dinewise.model.Student;
import com.example.dinewise.service.MealConfirmationService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

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



    // @GetMapping("/{stdId}/from/{date}")
    // public ResponseEntity<?> getStudentMealConfirmationsFromDate(
    //         @AuthenticationPrincipal Student student,
    //         @PathVariable String stdId,
    //         @PathVariable String date) {

    //     try {
    //         Student student = studentService.getStudentByStudentId(stdId);
    //         if (student == null) {
    //             return new ResponseEntity<>(new Message("Student not found"), HttpStatus.NOT_FOUND);
    //         }

    //         LocalDate startDate = LocalDate.parse(date);
    //         List<MealConfirmation> confirmations = service.getMealConfirmationsFromDate(student, startDate);

    //         return new ResponseEntity<>(confirmations, HttpStatus.OK);
    //     } catch (DateTimeParseException e) {
    //         return new ResponseEntity<>(new Message("Invalid date format. Use YYYY-MM-DD"), HttpStatus.BAD_REQUEST);
    //     }
    // }


    // @GetMapping("/get")
    // public ResponseEntity<?> getConfirmation(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    //                                          @RequestHeader("stdId") String stdId) {
    //     return service.getConfirmation(stdId, date)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("No meal confirmation found.")));
    // }
}
