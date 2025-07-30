package com.example.dinewise.controller;

import com.example.dinewise.model.Due;
import com.example.dinewise.model.Payment;
import com.example.dinewise.model.Student;
import com.example.dinewise.repo.DueRepository;
import com.example.dinewise.repo.PaymentRepository;
import com.example.dinewise.repo.StudentRepo;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private DueRepository dueRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private PaymentRepository paymentRepository;



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

    // @PostMapping("/success")
    // public Map<String, String> paymentSuccess(@RequestParam String tranId) {
    //     System.out.println("Payment successful for transaction ID: " + tranId);
    //     // Handle payment success
    //     return Map.of("status", "success", "transactionId", tranId);
    // }

    @PostMapping("/success/{tranId}")
    public void handlePaymentSuccess(
            @PathVariable("tranId") String tranId,
            @RequestParam Map<String, String> formData,
            HttpServletResponse response) throws IOException {

        // Log or process the formData
        System.out.println("Received transaction ID: " + tranId);
        formData.forEach((key, value) ->
                System.out.println(key + " = " + value)
        );

        // Validate, store in DB, or trigger your business logic here
        // Example: confirm payment, update due, etc.

        // return ResponseEntity.ok("Payment received");
        response.sendRedirect("http://52.184.83.81:8082/payment/success?tran_id=" + tranId);
    }

        @PostMapping("/success")
        public void handlePaymentSuccess(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
            System.out.println("Payment successful with params: " + params);
            try {
                String tranId = params.get("tran_id");
                String stdId = params.get("cus_id");

                Due due = dueRepo.findByStdId(stdId)
                        .orElseThrow(() -> new RuntimeException("Due not found"));

                double paidAmount = due.getTotalDue();

                // Update dues
                due.setTotalDue(0);
                due.setLastPaidDate(LocalDate.now());
                dueRepo.save(due);

                // Save to payment history
                Payment payment = new Payment();
                payment.setId(UUID.randomUUID());
                payment.setStdId(stdId);
                payment.setAmount(paidAmount);
                payment.setTransactionId(tranId);
                payment.setPaidAt(ZonedDateTime.now());
                paymentRepository.save(payment);

                // Redirect to frontend success page with transaction ID
                response.sendRedirect("http://52.184.83.81:8082/payment/success?tran_id=" + tranId);

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("http://52.184.83.81:8082/payment/fail");
            }
        }
    


    @PostMapping("/failure")
    public Map<String, String> paymentFailure(@RequestParam String tranId) {
        // Handle payment failure
        System.out.println("Payment failed");
        return Map.of("status", "failure", "transactionId", tranId);    
    }
    @PostMapping("/cancel")
    public Map<String, String> paymentCancel(@RequestParam String tranId) {
        // Handle payment cancellation
        System.out.println("Payment cancelled");
        return Map.of("status", "cancelled", "transactionId", tranId);
    }
}
