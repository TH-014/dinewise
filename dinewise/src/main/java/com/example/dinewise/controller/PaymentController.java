package com.example.dinewise.controller;

import com.example.dinewise.model.Due;
import com.example.dinewise.model.Student;
import com.example.dinewise.repo.DueRepository;
import com.example.dinewise.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private DueRepository dueRepo;

    @Autowired
    private StudentRepo studentRepo;

    @PostMapping("/initiate")
    public Map<String, String> initiatePayment(@RequestParam String stdId) {
        Optional<Student> optionalStudent = Optional.ofNullable(studentRepo.findByStdId(stdId));
        if (!optionalStudent.isPresent()) {
            throw new RuntimeException("Student not found");
        }
        Student student = optionalStudent.get();

        Due due = dueRepo.findByStdId(stdId).orElseThrow(() -> new RuntimeException("Due not found"));

        String tranId = "TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String totalAmount = String.valueOf(due.getTotalDue());

        TransactionInitiator initiator = new TransactionInitiator();
        String paymentURL = initiator.initTrnxnRequest(
                totalAmount,
                tranId,
                stdId,
                student.getFirstName(),
                student.getEmail(),
                student.getPhoneNumber()
        );

        return Map.of("paymentURL", paymentURL);
    }
}
