package com.example.dinewise.dto.response;

import com.example.dinewise.model.Student;

public class StudentResponseDTO {
    private String studentId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;


    public StudentResponseDTO(Student student) {
        this.studentId = student.getStdId();
        this.name = student.getFirstName();
        this.email = student.getEmail();
        this.phoneNumber = student.getPhoneNumber();
        this.address = student.getPresentAddress();

    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
