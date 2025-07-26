package com.example.dinewise.model;

public class CommentDto {
  private String menuDate;
  private Long parentId;
  private boolean anonymous;
  private String content;
  // getters / setters

  public String getMenuDate() {
    return menuDate;
  }

  public void setMenuDate(String menuDate) {
    this.menuDate = menuDate;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
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

  
}