package com.finance.project.final_project.codewave;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
  @ExceptionHandler(value = BusinessException.class)
  public String handleError(BusinessException e){
    return e.getMessage();
  }

  @ExceptionHandler(value = NullPointerException.class)
  public String handleNullPointerException(){
    return Syscode.RTE_NPE.getMessage();
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public String handleIllegalArgumentException(){
    return Syscode.ILLEGAL_ARG.getMessage();
  }
}
