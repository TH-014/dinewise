// package com.example.dinewise.model;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "comment_thread")
// public class CommentThread {
//   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//   private Long id;
//   private LocalDate menuDate;

//   @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
//   private List<Comment> comments = new ArrayList<>();
// }


package com.example.dinewise.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "comment_thread")
public class CommentThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate menuDate;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // ✅ Add this constructor
    public CommentThread(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    // ✅ Default constructor (needed by JPA)
    public CommentThread() {}

    // ✅ Getters and setters
    public Long getId() {
        return id;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

