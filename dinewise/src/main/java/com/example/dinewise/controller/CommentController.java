package com.example.dinewise.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dinewise.model.Comment;
import com.example.dinewise.model.CommentThread;
import com.example.dinewise.model.MessManager;
import com.example.dinewise.model.Student;
import com.example.dinewise.repo.CommentRepository;
import com.example.dinewise.repo.CommentThreadRepository;

import jakarta.validation.Valid;

import com.example.dinewise.dto.request.CommentDto;
import com.example.dinewise.dto.response.CommentResponseDto;

// @RestController
// @RequestMapping("/comments")
// public class CommentController {
//   @Autowired private CommentThreadRepository threadRepo;
//   @Autowired private CommentRepository commentRepo;
//   @Autowired private JwtGeneratorImpl auth; // your auth service

//   @GetMapping
//   public ResponseEntity<?> getComments(@RequestParam("date") String dateStr) {
//     LocalDate date = LocalDate.parse(dateStr);
//     CommentThread thread = threadRepo.findByMenuDate(date)
//          .orElseGet(() -> threadRepo.save(new CommentThread()));

//     List<Comment> all = commentRepo.findByThreadOrderByCreatedAtAsc(thread);
//     return ResponseEntity.ok(all);
//   }

//   @PostMapping
//   public ResponseEntity<?> postComment(@RequestBody CommentDto dto) {
//     UserPrincipal current = auth.getCurrentUser(); // get id, role
//     LocalDate date = LocalDate.parse(dto.getMenuDate());

//     CommentThread thread = threadRepo.findByMenuDate(date)
//       .orElseGet(() -> threadRepo.save(new CommentThread()));

//     Comment comment = new Comment();
//     comment.setThread(thread);
//     if (dto.getParentId() != null) {
//       comment.setParent(commentRepo.getReferenceById(dto.getParentId()));
//     }
//     comment.setAuthorId(current.getId());
//     comment.setAuthorRole(current.getRole().name());
//     comment.setAnonymous(dto.isAnonymous());
//     comment.setContent(dto.getContent());

//     commentRepo.save(comment);
//     return ResponseEntity.ok(comment);
//   }
// }



@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired private CommentThreadRepository threadRepo;
    @Autowired private CommentRepository commentRepo;

    @GetMapping
    public ResponseEntity<?> getComments(@Valid @RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        CommentThread thread = threadRepo.findByMenuDate(date)
            .orElseGet(() -> threadRepo.save(new CommentThread(date)));

        List<Comment> all = commentRepo.findByThreadOrderByCreatedAtAsc(thread);

        List<CommentResponseDto> dtos = all.stream()
        .map(c -> new CommentResponseDto(
            c.getId(),
            c.getParent() != null ? c.getParent().getId() : null,
            c.isAnonymous(),
            c.getAuthorId(),
            c.getAuthorRole(),
            c.getContent(),
            c.getCreatedAt()
        ))
        .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> postComment(
        @AuthenticationPrincipal Object user, // Can be Student or MessManager
        @RequestBody CommentDto dto
    ) {

      System.out.println("User: " + user);
        LocalDate date = LocalDate.parse(dto.getMenuDate());

        CommentThread thread = threadRepo.findByMenuDate(date)
            .orElseGet(() -> threadRepo.save(new CommentThread(date)));

        Comment comment = new Comment();
        comment.setThread(thread);

        if (dto.getParentId() != null) {
            comment.setParent(commentRepo.getReferenceById(dto.getParentId()));
        }

        if(user==null){
          System.out.println("User is null");
        }

        if (user instanceof Student student) {
            comment.setAuthorId(student.getStdId());
            comment.setAuthorRole("student");
        } else if (user instanceof MessManager manager) {
            comment.setAuthorId(manager.getStdId());
            comment.setAuthorRole("manager");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unknown user role");
        }

        comment.setAnonymous(dto.isAnonymous());
        comment.setContent(dto.getContent());

        commentRepo.save(comment);
        return ResponseEntity.ok(comment);
    }
}

