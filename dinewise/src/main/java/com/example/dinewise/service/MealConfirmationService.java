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

    // public MealConfirmationResponseDTO confirmMeal(String stdId, MealConfirmationRequestDTO dto) {
    //     Optional<MealConfirmation> existing = repository.findByStdIdAndMealDate(stdId, dto.getMealDate());

    //     MealConfirmation mealConfirmation = existing.orElse(new MealConfirmation());
    //     mealConfirmation.setStdId(stdId);
    //     mealConfirmation.setMealDate(dto.getMealDate());
    //     mealConfirmation.setWillLunch(dto.isWillLunch());
    //     mealConfirmation.setWillDinner(dto.isWillDinner());

    //     MealConfirmation saved = repository.save(mealConfirmation);

    //     // Update due
    //     Due due = dueRepository.findByStdId(stdId).orElseGet(() -> {
    //         Due newDue = new Due();
    //         newDue.setStdId(stdId);
    //         return newDue;
    //     });

    //     if (!existing.isPresent()) {
    //         double newCharge = 0;
    //         if (dto.isWillLunch()) newCharge += 50;
    //         if (dto.isWillDinner()) newCharge += 50;
    //         due.addToDue(newCharge);
    //     } else {
    //         MealConfirmation prev = existing.get();
    //         boolean prevLunch = prev.isWillLunch();
    //         boolean prevDinner = prev.isWillDinner();
    //         boolean newLunch = dto.isWillLunch();
    //         boolean newDinner = dto.isWillDinner();

    //         if (!prevLunch && newLunch) due.addToDue(50);
    //         if (prevLunch && !newLunch) due.subtractFromDue(50);
    //         if (!prevDinner && newDinner) due.addToDue(50);
    //         if (prevDinner && !newDinner) due.subtractFromDue(50);
    //     }

    //     return new MealConfirmationResponseDTO(saved);
    // }

    public MealConfirmationResponseDTO confirmMeal(String stdId, MealConfirmationRequestDTO dto) {
        Optional<MealConfirmation> existing = repository.findByStdIdAndMealDate(stdId, dto.getMealDate());

        MealConfirmation mealConfirmation = existing.orElse(new MealConfirmation());
        mealConfirmation.setStdId(stdId);
        mealConfirmation.setMealDate(dto.getMealDate());
        mealConfirmation.setWillLunch(dto.isWillLunch());
        mealConfirmation.setWillDinner(dto.isWillDinner());

        MealConfirmation saved = repository.save(mealConfirmation);

        // Update due
        Due due = dueRepository.findByStdId(stdId).orElseGet(() -> {
            Due newDue = new Due();
            newDue.setStdId(stdId);
            return newDue;
        });

        if (!existing.isPresent()) {
            double newCharge = 0;
            if (dto.isWillLunch()) newCharge += 50;
            if (dto.isWillDinner()) newCharge += 50;
            due.addToDue(newCharge);
        } else {
            MealConfirmation prev = existing.get();
            boolean prevLunch = prev.isWillLunch();
            boolean prevDinner = prev.isWillDinner();
            boolean newLunch = dto.isWillLunch();
            boolean newDinner = dto.isWillDinner();

            if (!prevLunch && newLunch) due.addToDue(50);
            if (prevLunch && !newLunch) due.subtractFromDue(50);
            if (!prevDinner && newDinner) due.addToDue(50);
            if (prevDinner && !newDinner) due.subtractFromDue(50);
        }

        // 🔧 Persist due updates
        dueRepository.save(due);

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

//     public List<MealConfirmation> getMealsAfterLastPayment(String stdId) {
//     Optional<Due> due = dueRepository.findByStdId(stdId);
//     if (due == null) {
//         throw new RuntimeException("Due or lastPaidDate not found for student ID: " + stdId);
//     }

//     LocalDate fromDate = due.getLastPaidDate();
//     return repository.findByStdIdAndMealDateGreaterThanEqual(stdId, fromDate);
// }
     
    public List<MealConfirmation> getMealsAfterLastPayment(String stdId) {
        Optional<Due> optionalDue = dueRepository.findByStdId(stdId);

        if (optionalDue.isEmpty() || optionalDue.get().getLastPaidDate() == null) {
            throw new RuntimeException("Due or lastPaidDate not found for student ID: " + stdId);
        }

        LocalDate fromDate = optionalDue.get().getLastPaidDate();
        return repository.findByStdIdAndMealDateGreaterThanEqual(stdId, fromDate);
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

