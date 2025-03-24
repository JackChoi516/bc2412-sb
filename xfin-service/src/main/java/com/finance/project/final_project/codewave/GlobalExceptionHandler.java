package com.finance.project.final_project.codewave;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleError(BusinessException e){
    return e.getMessage();
  }

  @ExceptionHandler(value = NullPointerException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleNullPointerException(){
    return Syscode.RTE_NPE.getMessage();
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleIllegalArgumentException(){
    return Syscode.ILLEGAL_ARG.getMessage();
  }
}
