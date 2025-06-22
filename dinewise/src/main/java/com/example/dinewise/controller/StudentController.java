package com.example.dinewise.controller;


import com.example.dinewise.config.JwtGeneratorInterface;
import com.example.dinewise.model.Student;
import com.example.dinewise.model.UserRequest;
import com.example.dinewise.repo.UserRequestRepository;
import com.example.dinewise.service.EmailSenderService;
import com.example.dinewise.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.http.HttpHeaders;
import jakarta.validation.Valid;
import com.example.dinewise.dto.request.LoginRequestDTO;
import com.example.dinewise.dto.request.SignupRequestDTO;
import com.example.dinewise.dto.response.Message;
import com.example.dinewise.dto.response.StudentResponseDTO;

@RestController
@RequestMapping("/")
public class StudentController {

    // @Autowired
    // StudentRepo studentRepo;

    // @PostMapping("/addStudent")
    // public String addStudents(@RequestBody Student student) {
    //     // Logic to add students can be implemented here
    //     studentRepo.save(student);
    //     return "Students added successfully!";
    // }

    private final StudentService studentService;
    private final JwtGeneratorInterface jwtGenerator;
    private final UserRequestRepository userRequestRepo;

    @Autowired
    private EmailSenderService senderService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // password encoder



    public StudentController(StudentService studentService, JwtGeneratorInterface jwtGenerator, UserRequestRepository userRequestRepo) {
        this.studentService = studentService;
        this.jwtGenerator = jwtGenerator;
        this.userRequestRepo = userRequestRepo;
        // this.senderService = new EmailSenderService(); // Assuming you have a default constructor
    }

    // @PostMapping("/login")
    // public ResponseEntity<?> loginStudent(@Valid @RequestBody LoginRequestDTO loginRequestDTO){

    //      try {
    //         if (loginRequestDTO.getStudentId() == null || loginRequestDTO.getPassword() == null) {
    //             return new ResponseEntity<>(new Message("Student Id or Password is Empty"), HttpStatus.CONFLICT);
    //         }

    //         // newStudent.setPasswordHash(passwordEncoder.encode(signupRequestDTO.getPassword()));

    //         // Student studentData = studentService.getStudentByUserNameAndPasswordHash(loginRequestDTO.getUserName(),
    //         //         passwordEncoder.encode(loginRequestDTO.getPassword()));

    //         // if (studentData == null) {
    //         //     return new ResponseEntity<>(new Message("UserName or Password is invalid"), HttpStatus.CONFLICT);
    //         // }

    //         Student studentData = studentService.getStudentByStudentId(loginRequestDTO.getStudentId());

    //         if (studentData == null || 
    //             !passwordEncoder.matches(loginRequestDTO.getPassword(), studentData.getPasswordHash())) {
    //             return new ResponseEntity<>(new Message("UserName or Password is invalid"), HttpStatus.CONFLICT);
    //         }

    //         // Generate the token map
    //         Map<String, String> tokenMap = jwtGenerator.generateToken(studentData);

    //         // Extract the JWT token
    //         String token = tokenMap.get("token");

    //         // Create HttpOnly cookie
    //         ResponseCookie cookie = ResponseCookie.from("authToken", token)
    //                 .httpOnly(true)
    //                 .secure(true) // true in production
    //                 .path("/") // Ensure cookie is available site-wide
    //                 .maxAge(24 * 60 * 60) // 1 day expiration
    //                 .sameSite("Lax") // Prevent CSRF attacks
    //                 .build();

    //         // Set the cookie in the response header
    //         HttpHeaders headers = new HttpHeaders();
    //         headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

    //         // show the token in the console  that it has been set to the headers for debugging

    //         // System.out.println("JWT Token set in cookie: " + token);

    //         System.out.println(headers.get(HttpHeaders.SET_COOKIE));


    //         Map<String, String> responseBody = new HashMap<>();
    //         responseBody.put("message", tokenMap.get("message"));
    //         responseBody.put("authToken", token); // Include token in body

    //         return ResponseEntity.ok()
    //                 .headers(headers) // still sets cookie if browser accepts
    //                 .body(responseBody);


    //         // return ResponseEntity.ok()
    //         //         .headers(headers)
    //         //         .body(new Message(tokenMap.get("message"))); // Sending back any message from the map
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    //     }

    // }

    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        try {
            if (loginRequestDTO.getStudentId() == null || loginRequestDTO.getPassword() == null) {
                return new ResponseEntity<>(new Message("Student Id or Password is Empty"), HttpStatus.CONFLICT);
            }

            Student studentData = studentService.getStudentByStudentId(loginRequestDTO.getStudentId());

            if (studentData == null || 
                !passwordEncoder.matches(loginRequestDTO.getPassword(), studentData.getPasswordHash())) {
                return new ResponseEntity<>(new Message("UserName or Password is invalid"), HttpStatus.CONFLICT);
            }

            // Generate token
            Map<String, String> tokenMap = jwtGenerator.generateToken(studentData);
            String token = tokenMap.get("token");

            // Create HttpOnly cookie
            ResponseCookie cookie = ResponseCookie.from("authToken", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("Lax")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            // Prepare response body
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", tokenMap.get("message"));
            responseBody.put("authToken", token);
            responseBody.put("student", new StudentResponseDTO(studentData)); // Include user data

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(responseBody);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }






    @PostMapping("/signup/request")
    public ResponseEntity<?> requestSignup(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        if (userRequestRepo.findByEmail(signupRequestDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Email already requested"));
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        UserRequest req = new UserRequest();
        req.setUsername(signupRequestDTO.getUsername());
        req.setEmail(signupRequestDTO.getEmail());
        req.setPasswordHash(passwordEncoder.encode(signupRequestDTO.getPassword()));
        req.setStdId(signupRequestDTO.getStdId());
        req.setFirstName(signupRequestDTO.getFirstName());
        req.setLastName(signupRequestDTO.getLastName());
        req.setPhoneNumber(signupRequestDTO.getPhoneNumber());
        req.setImageUrl(signupRequestDTO.getImageUrl());
        req.setPresentAddress(signupRequestDTO.getPresentAddress());
        req.setPermanentAddress(signupRequestDTO.getPermanentAddress());
        req.setOtp(otp);
        req.setOtpExpiry(expiry);

        userRequestRepo.save(req);

        senderService.sendSimpleEmail(
            signupRequestDTO.getEmail(),
            "Dinewise OTP Verification",
            "Your OTP is: " + otp + "\nIt is valid for 10 minutes."
        );

        return ResponseEntity.ok(new Message("OTP sent to your email"));
    }



    @PostMapping("/signup/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");

        Optional<UserRequest> optional = userRequestRepo.findByEmail(email);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Request not found"));
        }

        UserRequest request = optional.get();

        if (request.isVerified()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Already verified"));
        }

        if (request.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("OTP expired"));
        }

        if (!request.getOtp().equals(otp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Invalid OTP"));
        }

        // Create the Student
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPasswordHash(request.getPasswordHash());
        student.setEmail(request.getEmail());
        student.setStdId(request.getStdId());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setImageUrl(request.getImageUrl());
        student.setPresentAddress(request.getPresentAddress());
        student.setPermanentAddress(request.getPermanentAddress());

        studentService.saveStudent(student);
        request.setVerified(true);
        userRequestRepo.save(request);

        return ResponseEntity.ok(new Message("Signup complete. You may now log in."));
    }







    
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            Student existingStudent = studentService.getStudentByStudentId(signupRequestDTO.getStdId());

            if (existingStudent != null) {
                Message error = new Message("Student Id is already taken");
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }

            existingStudent = studentService.getStudentByEmail(signupRequestDTO.getEmail());
            if (existingStudent != null) {
                Message error = new Message("Email is already registered");
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }

            Student newStudent = new Student();
            newStudent.setUsername(signupRequestDTO.getUsername());
             newStudent.setPasswordHash(passwordEncoder.encode(signupRequestDTO.getPassword()));
            // newStudent.setPasswordHash(signupRequestDTO.getPassword()); // Remember to hash the password before saving
            newStudent.setEmail(signupRequestDTO.getEmail());
            newStudent.setStdId(signupRequestDTO.getStdId());
            newStudent.setFirstName(signupRequestDTO.getFirstName());
            newStudent.setLastName(signupRequestDTO.getLastName());
            newStudent.setPhoneNumber(signupRequestDTO.getPhoneNumber());
            newStudent.setImageUrl(signupRequestDTO.getImageUrl());
            newStudent.setPresentAddress(signupRequestDTO.getPresentAddress());
            newStudent.setPermanentAddress(signupRequestDTO.getPermanentAddress());

            studentService.saveStudent(newStudent);

            // Generate a JWT token for the new user
            // String token = jwtGenerator.generateToken(newUser);

            return new ResponseEntity<>(newStudent, HttpStatus.OK);
        } catch (Exception e) {
            Message error = new Message("An error occurred during the signup process");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
