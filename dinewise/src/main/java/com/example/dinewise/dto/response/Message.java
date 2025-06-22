package com.example.dinewise.dto.response;


public class Message {
  
  private String text;
  public Message()
  {
    this.text="";
  }
  

  public Message(String text)
  {
    this.text=text;
  }

  public String getText()
  {
    return text;
  }
    
  public void setText(String text)
  {
    this.text=text;
  }
}
