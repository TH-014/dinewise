package com.example.dinewise.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.CommentThread;

public interface CommentThreadRepository extends JpaRepository<CommentThread, Long> {
  Optional<CommentThread> findByMenuDate(LocalDate date);
}
