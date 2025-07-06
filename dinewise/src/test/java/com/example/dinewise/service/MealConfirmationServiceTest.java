package com.example.dinewise.service;

import com.example.dinewise.dto.request.MealConfirmationRequestDTO;
import com.example.dinewise.dto.response.MealConfirmationResponseDTO;
import com.example.dinewise.model.MealConfirmation;
import com.example.dinewise.repo.MealConfirmationRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MealConfirmationServiceTest {

    @Mock
    private MealConfirmationRepository repository;

    @InjectMocks
    private MealConfirmationService service;

    public MealConfirmationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConfirmMeal_NewEntry() {
        MealConfirmationRequestDTO request = new MealConfirmationRequestDTO();
        request.setMealDate(LocalDate.now().plusDays(1));
        request.setWillLunch(true);
        request.setWillDinner(false);

        String studentId = "2005011";

        when(repository.findByStdIdAndMealDate(eq(studentId), any())).thenReturn(Optional.empty());

        MealConfirmation saved = new MealConfirmation();
        saved.setId(1L);
        saved.setStdId(studentId);
        saved.setMealDate(request.getMealDate());
        saved.setWillLunch(true);
        saved.setWillDinner(false);

        when(repository.save(any(MealConfirmation.class))).thenReturn(saved);

        MealConfirmationResponseDTO response = service.confirmMeal(studentId, request);

        assertNotNull(response);
        assertEquals(request.getMealDate(), response.getMealDate());
        assertTrue(response.isWillLunch());
        assertFalse(response.isWillDinner());

        verify(repository, times(1)).save(any());
    }
}

