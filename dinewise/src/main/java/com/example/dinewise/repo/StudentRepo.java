package com.example.dinewise.repo;

import com.example.dinewise.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface StudentRepo extends JpaRepository<Student, UUID> {


}
