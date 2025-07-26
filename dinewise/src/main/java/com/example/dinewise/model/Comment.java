package com.example.dinewise.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "comment")
public class Comment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "thread_id", nullable = false)
  private CommentThread thread;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Comment parent; // null if top-level

  private String authorId;
  private String authorRole;
  private boolean anonymous;
  @Column(columnDefinition = "TEXT")
  private String content;
  private Instant createdAt = Instant.now();

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> replies = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CommentThread getThread() {
    return thread;
  }

  public void setThread(CommentThread thread) {
    this.thread = thread;
  }

  public Comment getParent() {
    return parent;
  }

  public void setParent(Comment parent) {
    this.parent = parent;
  }

  public String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public String getAuthorRole() {
    return authorRole;
  }

  public void setAuthorRole(String authorRole) {
    this.authorRole = authorRole;
  }

  public boolean isAnonymous() {
    return anonymous;
  }

  public void setAnonymous(boolean anonymous) {
    this.anonymous = anonymous;
  }

  public String getContent() {
    return content;
  } 

  public void setContent(String content) {
    this.content = content;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }


  public List<Comment> getReplies() {
    return replies;
  }

  public void setReplies(List<Comment> replies) {
    this.replies = replies;
  }

  public void addReply(Comment reply) {
    replies.add(reply);
    reply.setParent(this);
  }

  public void removeReply(Comment reply) {
    replies.remove(reply);
    reply.setParent(null);

  }

  
}
