package com.example.dinewise.service;



import com.example.dinewise.model.ApplicationStatus;
import com.example.dinewise.model.ManagerApplication;
import com.example.dinewise.model.Student;
import com.example.dinewise.repo.ManagerApplicationRepo;
import com.example.dinewise.repo.StudentRepo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private ManagerApplicationRepo applicationRepo;



    public Student getStudentByUsername(String username) {
        return studentRepo.findByUsername(username);
    }
    

    public Student getStudentByName(String name) {
        return studentRepo.findByFirstName(name);
    }
    /**
     * Retrieves a student by their ID.
     *
     * @param id the ID of the student
     * @return the Student object if found, otherwise null
     */
    public Student getStudentByStudentId(String id) {
        return studentRepo.findByStdId(id);
    }

    /**
     * Retrieves a student by their username and password.
     *
     * @param username the username of the student
     * @param password the password of the student
     * @return the Student object if found, otherwise null
     */
    public Student getStudentByUserNameAndPasswordHash(String username, String password) {
        return studentRepo.findByUsernameAndPasswordHash(username, password);
    }


    /**
     * Retrieves a student by their email.
     *
     * @param email the email of the student
     * @return the Student object if found, otherwise null
     */

    public Student getStudentByEmail(String email) {
        return studentRepo.findByEmail(email);
    }

    /**
     * Saves a student to the repository.
     *
     * @param student the Student object to be saved
     * @return the saved Student object
     */
    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }



      public boolean applyForManager(String stdId, LocalDate appliedMonth) {
        if (applicationRepo.existsByStdIdAndAppliedMonth(stdId, appliedMonth)) return false;

        ManagerApplication application = new ManagerApplication();
        application.setStdId(stdId);
        application.setAppliedMonth(appliedMonth);
        application.setStatus(ApplicationStatus.pending);
        application.setReviewedAt(null);

        applicationRepo.save(application);
        return true;
    }







}
