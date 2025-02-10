package com.bootcamp.demo.demo_sb_customer.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = BusinessException.class)
  public ErrorResult handleError(BusinessException e){
    return new ErrorResult(e.getMessage());
  }


}
