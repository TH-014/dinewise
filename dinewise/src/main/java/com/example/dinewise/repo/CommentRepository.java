package com.example.dinewise.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinewise.model.Comment;
import com.example.dinewise.model.CommentThread;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByThreadOrderByCreatedAtAsc(CommentThread thread);
}