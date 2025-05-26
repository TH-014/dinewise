package com.example.dinewise.controller;


import com.example.dinewise.model.Student;
import com.example.dinewise.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @Autowired
    StudentRepo studentRepo;

    @PostMapping("/addStudent")
    public String addStudents(@RequestBody Student student) {
        // Logic to add students can be implemented here
        studentRepo.save(student);
        return "Students added successfully!";
    }
}
