package com.example.dinewise.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dinewise.model.ApplicationStatus;
import com.example.dinewise.model.ManagerApplication;
import com.example.dinewise.model.ManagerStatus;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.repo.ManagerApplicationRepo;
import com.example.dinewise.repo.MessManagerRepo;
import com.example.dinewise.repo.StudentRepo;

@Service
public class MessManagerService {

    @Autowired
    private ManagerApplicationRepo applicationRepo;

    @Autowired
    private MessManagerRepo messManagerRepo;

    @Autowired
    private StudentRepo studentRepo;

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

    

    public MessManager getActiveManager(String stdId) {
        return messManagerRepo.findByStdIdAndStatus(stdId, ManagerStatus.running).orElse(null);
    }

    public MessManager getByStdId(String stdId) {
        return messManagerRepo.findByStdId(stdId).orElse(null);
    }
}

