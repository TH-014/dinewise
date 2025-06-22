package com.example.dinewise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dinewise.dto.request.EmailRequest;
import com.example.dinewise.service.EmailSenderService;

import jakarta.validation.Valid;

// @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
	private EmailSenderService senderService;

    @PostMapping("/sendMail")
    public ResponseEntity<String> changePassword(@Valid @RequestBody EmailRequest emailRequest) {
        senderService.sendSimpleEmail(emailRequest.getRecipient(),
        emailRequest.getSubject(),
        emailRequest.getBody());
        return ResponseEntity.ok("Mail Sent SucceessFully");
    }
}
