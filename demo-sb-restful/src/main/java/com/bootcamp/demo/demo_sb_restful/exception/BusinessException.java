package com.bootcamp.demo.demo_sb_restful.exception;

public class BusinessException extends RuntimeException{
  
  public BusinessException(String message){
    super(message);
  }
}
