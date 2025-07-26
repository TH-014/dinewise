// src/main/java/com/example/dinewise/dto/response/CommentResponseDto.java
package com.example.dinewise.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long id;
    private Long parentId;
    private boolean anonymous;
    private String authorId;
    private String authorRole;
    private String content;
    private Instant createdAt;

    public CommentResponseDto() {}

    public CommentResponseDto(Long id, Long parentId, boolean anonymous, String authorId, String authorRole, String content, Instant createdAt) {
        this.id = id;
        this.parentId = parentId;
        this.anonymous = anonymous;
        this.authorId = authorId;
        this.authorRole = authorRole;
        this.content = content;
        this.createdAt = createdAt;
    }

    // ✅ Getters and Setters for all fields

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getAuthorRole() { return authorRole; }
    public void setAuthorRole(String authorRole) { this.authorRole = authorRole; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

