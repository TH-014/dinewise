package com.example.dinewise.service;
import com.example.dinewise.dto.request.MealConfirmationRequestDTO;
import com.example.dinewise.dto.response.MealConfirmationResponseDTO;
import com.example.dinewise.model.Due;
import com.example.dinewise.model.MealConfirmation;
import com.example.dinewise.model.Student;
import com.example.dinewise.repo.DueRepository;
import com.example.dinewise.repo.MealConfirmationRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MealConfirmationService {

    private final MealConfirmationRepository repository;
    private final DueRepository dueRepository;

    public MealConfirmationService(MealConfirmationRepository repository, DueRepository dueRepository) {
        this.repository = repository;
        this.dueRepository = dueRepository;
    }

    public MealConfirmationResponseDTO confirmMeal(String stdId, MealConfirmationRequestDTO dto) {
        Optional<MealConfirmation> existing = repository.findByStdIdAndMealDate(stdId, dto.getMealDate());

        MealConfirmation mealConfirmation = existing.orElse(new MealConfirmation());
        mealConfirmation.setStdId(stdId);
        mealConfirmation.setMealDate(dto.getMealDate());
        mealConfirmation.setWillLunch(dto.isWillLunch());
        mealConfirmation.setWillDinner(dto.isWillDinner());

        MealConfirmation saved = repository.save(mealConfirmation);
        // Determine meal price (50 per meal)
        int newMealCount = (dto.isWillLunch() ? 1 : 0) + (dto.isWillDinner() ? 1 : 0);
        int oldMealCount = existing.map(mc -> (mc.isWillLunch() ? 1 : 0) + (mc.isWillDinner() ? 1 : 0)).orElse(0);
        int mealDifference = newMealCount - oldMealCount;
        double amountChange = mealDifference * 50.0;

        // Update due
        Due due = dueRepository.findByStdId(stdId).orElseGet(() -> {
            Due newDue = new Due();
            newDue.setStdId(stdId);
            return newDue;
        });

        if (amountChange != 0) {
            if (amountChange > 0) {
                due.addToDue(amountChange);
            } else {
                due.subtractFromDue(Math.abs(amountChange));
            }
            dueRepository.save(due);
        }
        return new MealConfirmationResponseDTO(saved);
    }



    public Optional<MealConfirmationResponseDTO> getConfirmation(String stdId, LocalDate mealDate) {
        return repository.findByStdIdAndMealDate(stdId, mealDate)
                .map(MealConfirmationResponseDTO::new);
    }

    public Optional<MealConfirmation> getMealConfirmationByMealId(Long id){
        return repository.findById(id);
    }

    public List<MealConfirmation> getMealConfirmationsFromDate(String std_id, LocalDate date) {
        return repository.findByStdIdAndMealDateGreaterThanEqual(std_id, date);
    }

    public void deleteMealConfirmation(Long id) {
        Optional<MealConfirmation> existing = repository.findById(id);
        if (existing.isPresent()) {
            MealConfirmation mc = existing.get();
            int count = (mc.isWillLunch() ? 1 : 0) + (mc.isWillDinner() ? 1 : 0);
            double amountToSubtract = count * 50.0;

            // Update dues
            Due due = dueRepository.findByStdId(mc.getStdId()).orElse(null);
            if (due != null) {
                due.subtractFromDue(amountToSubtract);
                dueRepository.save(due);
            }

            repository.deleteById(id);
        }
    }    

    public Due getDueByStudentId(String stdId) {
        return dueRepository.findByStdId(stdId).orElse(null);
    }

}

