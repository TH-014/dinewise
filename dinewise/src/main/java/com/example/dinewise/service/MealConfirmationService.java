package com.example.dinewise.service;
import com.example.dinewise.dto.request.MealConfirmationRequestDTO;
import com.example.dinewise.dto.response.MealConfirmationResponseDTO;
import com.example.dinewise.model.MealConfirmation;
import com.example.dinewise.repo.MealConfirmationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MealConfirmationService {

    private final MealConfirmationRepository repository;

    public MealConfirmationService(MealConfirmationRepository repository) {
        this.repository = repository;
    }

    public MealConfirmationResponseDTO confirmMeal(String stdId, MealConfirmationRequestDTO dto) {
        Optional<MealConfirmation> existing = repository.findByStdIdAndMealDate(stdId, dto.getMealDate());

        MealConfirmation mealConfirmation = existing.orElse(new MealConfirmation());
        mealConfirmation.setStdId(stdId);
        mealConfirmation.setMealDate(dto.getMealDate());
        mealConfirmation.setWillLunch(dto.isWillLunch());
        mealConfirmation.setWillDinner(dto.isWillDinner());

        MealConfirmation saved = repository.save(mealConfirmation);
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
        repository.deleteById(id); // Here id is the Long type ID of the MealConfirmation
    }

    

}

