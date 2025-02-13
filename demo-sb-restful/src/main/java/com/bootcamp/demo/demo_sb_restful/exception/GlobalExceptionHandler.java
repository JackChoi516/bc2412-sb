package com.bootcamp.demo.demo_sb_restful.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bootcamp.demo.demo_sb_restful.model.ErrorResult;

@RestControllerAdvice // bean
public class GlobalExceptionHandler { // 
  
  @ExceptionHandler(value = {ArithmeticException.class, BusinessException.class})
  @ResponseStatus(value = HttpStatus.OK)
  public ErrorResult handleArithmetic(RuntimeException e){
    return new ErrorResult(e.getMessage());
  }

  // @ExceptionHandler(value = BusinessException.class)
  // @ResponseStatus(value = HttpStatus.OK)
  // public ErrorResult handleArithmetic(BusinessException e){
  //   return new ErrorResult(e.getMessage());
  // }
}
