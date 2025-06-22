package com.example.dinewise.controller;

import com.example.dinewise.dto.request.ManagerApplicationRequestDTO;
import com.example.dinewise.dto.request.ManagerLoginRequestDTO;
import com.example.dinewise.dto.response.Message;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Student;
import com.example.dinewise.repo.MealConfirmationRepository;
import com.example.dinewise.config.JwtGeneratorImpl;
import com.example.dinewise.service.MessManagerService;
import com.example.dinewise.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/manager")
public class MessManagerController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGeneratorImpl jwtGenerator;

    @Autowired
    private MessManagerService messManagerService;

    @Autowired
    private MealConfirmationRepository mealConfirmationRepository;

    @PostMapping("/login")
    public ResponseEntity<?> loginManager(@RequestBody ManagerLoginRequestDTO request) {
        System.out.println(request.getStdId());
        Student student = studentService.getStudentByStudentId(request.getStdId());
        System.out.println(student);
        if (student == null || !passwordEncoder.matches(request.getPassword(), student.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message("Invalid credentials"));
        }

        MessManager manager = messManagerService.getActiveManager(student.getStdId());  // It will show the running mess managers only
        if (manager == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Message("Not an active mess manager"));
        }

        Map<String, String> tokenMap = jwtGenerator.generateToken(student.getStdId(), "manager"); // You may create a separate role if needed
        String token = tokenMap.get("token");

        ResponseCookie cookie = ResponseCookie.from("authToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(Map.of("message", "Manager login successful", "authToken", token));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getNextDayMealCounts() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        long lunchCount = mealConfirmationRepository.countByMealDateAndWillLunchTrue(tomorrow);
        long dinnerCount = mealConfirmationRepository.countByMealDateAndWillDinnerTrue(tomorrow);

        return ResponseEntity.ok(Map.of(
            "lunchCount", lunchCount,
            "dinnerCount", dinnerCount
        ));
    }


    @PostMapping("/apply")
    public ResponseEntity<?> applyAsManager(@RequestBody ManagerApplicationRequestDTO request) {
        boolean success = messManagerService.applyForManager(request.getStdId(), request.getAppliedMonth());
        if (!success) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Already applied for this month"));
        }
        return ResponseEntity.ok(new Message("Application submitted successfully"));
    }
}

