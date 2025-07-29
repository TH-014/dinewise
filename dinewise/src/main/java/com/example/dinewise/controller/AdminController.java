package com.example.dinewise.controller;

import com.example.dinewise.dto.request.AdminLoginRequestDTO;
import com.example.dinewise.dto.response.ManagerApplicationResponseDTO;
import com.example.dinewise.dto.response.Message;
import com.example.dinewise.model.Admin;
import com.example.dinewise.model.ApplicationStatus;
import com.example.dinewise.model.ManagerApplication;
import com.example.dinewise.model.ManagerStatus;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.repo.AdminRepository;
import com.example.dinewise.repo.ManagerApplicationRepo;
import com.example.dinewise.repo.MessManagerRepo;
import com.example.dinewise.config.JwtGeneratorImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGeneratorImpl jwtGenerator;

    @Autowired
    private ManagerApplicationRepo applicationRepo;

    @Autowired
    private MessManagerRepo messManagerRepo;

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminLoginRequestDTO request) {
        Admin admin = adminRepository.findByUsername(request.getUsername()).orElse(null);

        if (admin == null || !passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message("Invalid credentials"));
        }

        Map<String, String> tokenMap = jwtGenerator.generateToken(admin.getUsername(), "admin");
        String token = tokenMap.get("token");

        ResponseCookie cookie = ResponseCookie.from("authToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Admin login successful", "authToken", token));
    }

    @GetMapping("/applications")
    public List<ManagerApplicationResponseDTO> getAllApplications() {
        List<ManagerApplication> applications = applicationRepo.findAll();

        // just take those requests which are pending
        applications = applications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.pending)
                .collect(Collectors.toList());
        if (applications.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pending applications found");
        }

        return applications.stream().map(app -> {
            ManagerApplicationResponseDTO dto = new ManagerApplicationResponseDTO();
            dto.setId(app.getId());
            dto.setStdId(app.getStdId());
            dto.setAppliedMonth(app.getAppliedMonth().toString());
            dto.setStatus(app.getStatus().toString());
            dto.setReviewedAt(app.getReviewedAt() != null ? app.getReviewedAt().toString() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    

    // @GetMapping("/applications")
    // public ResponseEntity<List<ManagerApplication>> getPendingApplications() {
    //     return ResponseEntity.ok(applicationRepo.findByStatus(ApplicationStatus.pending));
    // }

    @PostMapping("/applications/{id}/approve")
    public ResponseEntity<?> approveApplication(@PathVariable Long id) {
        Optional<ManagerApplication> optionalApp = applicationRepo.findById(id);
        if (optionalApp.isEmpty()) return ResponseEntity.notFound().build();

        ManagerApplication app = optionalApp.get();
        app.setStatus(ApplicationStatus.approved);
        app.setReviewedAt(ZonedDateTime.now(ZoneId.of("Asia/Dhaka")));

        // Create MessManager record
        LocalDate firstDay = app.getAppliedMonth().withDayOfMonth(1);
        LocalDate lastDay = YearMonth.from(firstDay).atEndOfMonth();

        MessManager manager = new MessManager();
        manager.setStdId(app.getStdId());
        manager.setStatus(ManagerStatus.upcoming);
        manager.setStartDate(firstDay);
        manager.setEndDate(lastDay);
        manager.setAvgRating(0.0);

        applicationRepo.save(app);
        messManagerRepo.save(manager);

        return ResponseEntity.ok("Application approved and mess manager scheduled.");
    }

    @PostMapping("/applications/{id}/reject")
    public ResponseEntity<?> rejectApplication(@PathVariable Long id) {
        Optional<ManagerApplication> optionalApp = applicationRepo.findById(id);
        if (optionalApp.isEmpty()) return ResponseEntity.notFound().build();

        ManagerApplication app = optionalApp.get();
        app.setStatus(ApplicationStatus.rejected);
        app.setReviewedAt(ZonedDateTime.now(ZoneId.of("Asia/Dhaka")));
        applicationRepo.save(app);

        return ResponseEntity.ok("Application rejected.");
    }


}
