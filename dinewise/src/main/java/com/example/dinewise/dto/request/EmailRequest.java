package com.example.dinewise.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailRequest {
    
    @NotBlank(message = "Recipient email is mandatory")
    @Email(message = "Recipient email should be valid")
    private String recipient;

    @NotBlank(message = "Email body is mandatory")
    private String body;

    @NotBlank(message = "Email subject is mandatory")
    private String subject;

    // Getters and Setters
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

