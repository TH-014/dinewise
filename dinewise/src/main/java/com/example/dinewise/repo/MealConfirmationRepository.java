package com.example.dinewise.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.dinewise.model.MealConfirmation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealConfirmationRepository extends JpaRepository<MealConfirmation, Long> {

    Optional<MealConfirmation> findByStdIdAndMealDate(String stdId, LocalDate mealDate);
    List<MealConfirmation> findByStdIdAndMealDateGreaterThanEqual(String std_id, LocalDate date);
    // List<MealConfirmation> findByStudentAndMealDateBetween(String std_id, LocalDate startDate, LocalDate endDate);
    List<MealConfirmation> findByStdId(String std_id);
    Optional<MealConfirmation> findById(Long id);
    void deleteById(Long id);
    long countByMealDateAndWillLunchTrue(LocalDate mealDate);
    long countByMealDateAndWillDinnerTrue(LocalDate mealDate);
    

}
