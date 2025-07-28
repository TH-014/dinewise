// package com.example.dinewise.controller;
// import com.example.dinewise.dto.request.ItemStatsDto;
// import com.example.dinewise.dto.request.MenuRequestDTO;
// import com.example.dinewise.model.Menu;
// import com.example.dinewise.model.MessManager;
// import com.example.dinewise.repo.MenuRepository;
// import com.example.dinewise.service.MenuService;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.Set;
// import java.util.HashSet;
// import java.util.ArrayList;
// import java.util.stream.Collectors;
// import com.example.dinewise.model.Stock;
// import com.example.dinewise.repo.StockRepository;
// import com.example.dinewise.model.MealConfirmation;
// import com.example.dinewise.repo.MealConfirmationRepository;
// import com.example.dinewise.service.AiService;
// // import com.example.dinewise.service.AiPromptBuilder;
// import com.example.dinewise.service.GeminiService;



// @RestController
// @RequestMapping("/ai/menu-suggestion")
// @RequiredArgsConstructor
// public class AiMenuController {
//     @Autowired
//     private final MenuRepository menuRepo;
//     @Autowired
//     private final StockRepository stockRepo;
//     @Autowired
//     private final MealConfirmationRepository confirmationRepo;
//     @Autowired
//     private final AiService aiService;


   


//     @GetMapping
//     public ResponseEntity<?> suggestMenu(
//         @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
//         @RequestParam("mode") String mode,              // normal, improve-lunch, improve-dinner, both, fest
//         @RequestParam("allowShopping") boolean allowShopping
//     ) {
//         // Step 1: Get last 5 menus
//         List<Menu> recentMenus = menuRepo.findTop5ByMenuDateLessThanOrderByMenuDateDesc(date);

//         // Step 2: Get current stock
//         List<Stock> stocks = stockRepo.findAll();

//         // Step 3: Get student counts
//         long lunchCount = confirmationRepo.countByMealDateAndWillDinnerTrue(date);
//         long dinnerCount = confirmationRepo.countByMealDateAndWillDinnerTrue(date);

//         // Step 4: Build prompt
//         String prompt = AiPromptBuilder.buildPrompt(recentMenus, stocks, lunchCount, dinnerCount, mode, allowShopping);

//         // Step 5: Ask AI
//         String aiResponse = aiService.getSuggestedMenu(prompt);

//         return ResponseEntity.ok(Map.of("suggestion", aiResponse));
//     }
// }


package com.example.dinewise.controller;

import com.example.dinewise.model.Menu;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Stock;
import com.example.dinewise.repo.MenuRepository;
import com.example.dinewise.repo.StockRepository;
import com.example.dinewise.repo.MealConfirmationRepository;
import com.example.dinewise.service.GeminiService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/menu-suggestion")
@RequiredArgsConstructor
public class AiMenuController {

  
    private final MenuRepository menuRepo;
    private final StockRepository stockRepo;
    private final MealConfirmationRepository confirmationRepo;
    private final GeminiService geminiService;

    @GetMapping
    public ResponseEntity<?> suggestMenu(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam("mode") String mode, // e.g., normal, improve-lunch, etc.
        @RequestParam("allowShopping") boolean allowShopping,
        @AuthenticationPrincipal MessManager manager // Assuming you have a MessManager entity for authentication
    ) {
        // Step 1: Fetch previous 5 menus
        List<Menu> recentMenus = menuRepo.findTop5ByMenuDateLessThanOrderByMenuDateDesc(date);

        // Step 2: Fetch current stock
        List<Stock> stocks = stockRepo.findAll();

        // Step 3: Fetch confirmed meal counts
        long lunchCount = confirmationRepo.countByMealDateAndWillLunchTrue(date);
        long dinnerCount = confirmationRepo.countByMealDateAndWillDinnerTrue(date);

        // Step 4: Build the AI prompt
        String prompt = AiPromptBuilder.buildPrompt(recentMenus, stocks, lunchCount, dinnerCount, mode, allowShopping);

        // Step 5: Call Gemini API
        String aiResponse = geminiService.getSuggestedMenu(prompt);

        return ResponseEntity.ok(Map.of("suggestion", aiResponse));
    }
}

