package com.bootcamp.ex.sb_exercise1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bootcamp.ex.sb_exercise1.model.ErrorResult;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(value = {ArithmeticException.class, IllegalArgumentException.class, NumberFormatException.class})
  @ResponseStatus(value = HttpStatus.OK)
  public ErrorResult errorResult (RuntimeException e){
    return new ErrorResult("Invalid input!!!");
  }
}
